package io.xmljim.json.jsonpath.function;

import io.xmljim.json.jsonpath.JsonPathException;
import io.xmljim.json.jsonpath.compiler.JsonPathExpressionException;
import io.xmljim.json.jsonpath.function.info.FunctionInfo;
import io.xmljim.json.jsonpath.util.Global;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FunctionFactory {

    private static final String FUNCTION_PATTERN = "(?<function>[a-z0-9\\-]+)\\((?<args>.*)\\)";
    private static final String ARGS_PATTERN = "(([@$.a-zA-Z\\d_\\-'{}*#]+(\\[[,:a-z\\d'-]+])*)(\\s[!<>=a-z]+\\s\\2?)?(\\s?[&|]{2}\\s?)?)+";//"(([@$.a-zA-Z0-9_\\-'{}#\\[,:\\]]+)(\\s?[=!<>a-z]+\\s)?([@$.a-zA-Z0-9_\\-'{}#\\[,:\\]]+)?(\\s?[&|]+\\s?)?([@$.a-zA-Z0-9_\\-'{}#\\[,:\\]]+)(\\s?[=!<>a-z]+\\s)?([@$.a-zA-Z0-9_\\-'{}#\\[,\\]]+)?)(,\\s?((([@$.a-zA-Z0-9_\\-'{}#\\[,:\\]]+)(\\s?[=!<>a-z]+\\s)?([@$.a-zA-Z0-9_\\-'{}#\\[,:\\]]+)?(\\s?[&|]+\\s?)?([@$.a-zA-Z0-9_\\-'{}#\\[,:\\]]+)(\\s?[=!<>a-z]+\\s)?([@$.a-zA-Z0-9_\\-'{}#\\[,:\\]]+)?)))*";

    public static JsonPathFunction createFunction(String expression, Global global) {
        Pattern functionPattern = Pattern.compile(FUNCTION_PATTERN);
        Matcher matcher = functionPattern.matcher(expression);

        if (matcher.matches()) {
            String fxName = matcher.group("function");

            FunctionInfo functionInfo = global.getFunctionRegistry()
                    .getFunctionInfo(fxName)
                    .orElseThrow(() -> new JsonPathException("Function not found: " + fxName));

            String argsString = matcher.group("args");

            if (argsString != null && !"".equals(argsString)) {
                List<Argument<?, ?>> args = buildArgs(functionInfo, argsString, global);
                return newInstance(functionInfo.functionClass(), args, global);
            } else {
                return newInstance(functionInfo.functionClass(), Collections.emptyList(), global);
            }
        } else {
            throw new JsonPathExpressionException(expression, 0, "Undefined function");
        }
    }

    private static List<Argument<?, ?>> buildArgs(FunctionInfo functionInfo, String argsString, Global global) {
        List<Argument<?, ?>> args = new ArrayList<>();
        Pattern argPattern = Pattern.compile(ARGS_PATTERN);
        Matcher matcher = argPattern.matcher(argsString);
        //put the elements into a stack for handling with arguments
        Deque<String> argExpressions = new ArrayDeque<>();

        while (matcher.find()) {
            if (matcher.group() != null && !"".equals(matcher.group())) {
                argExpressions.add(matcher.group().strip().endsWith(",") ?
                        matcher.group().strip().substring(0, matcher.group().strip().length() - 1) :
                        matcher.group().strip());
            }
        }
        if (functionInfo.arguments().length != 0) {
            //now start iterating over the arguments
            Arrays.stream(functionInfo.arguments()).forEach(argumentInfo -> {
                switch (argumentInfo.scope()) {
                    case REQUIRED -> {
                        if (argExpressions.peek() != null) {
                            args.add(Arguments.newArgument(argExpressions.pop(), argumentInfo, global));
                        } else {
                            throw new JsonPathException("Expected required argument [" + argumentInfo.name() + "] not found");
                        }
                    }
                    case OPTIONAL -> {
                        if (argExpressions.peek() != null) {
                            args.add(Arguments.newArgument(argExpressions.pop(), argumentInfo, global));
                        }
                    }
                    case VARARGS -> { //greedy inclusion
                        while (!argExpressions.isEmpty()) {
                            args.add(Arguments.newArgument(argExpressions.pop(), argumentInfo, global));
                        }
                    }
                }
            });

            //make sure we don't have any lingering argument expressions...
            if (!argExpressions.isEmpty()) {
                throw new JsonPathException("Unexpected argument for function " + argExpressions.pop());
            }
        } else {
            if (!argExpressions.isEmpty()) {
                throw new JsonPathException("Unexpected argument for function " + functionInfo + "; arg = " + argExpressions.pop());
            }
        }

        return args;
    }

    @SuppressWarnings("unchecked")
    private static JsonPathFunction newInstance(Class<? extends JsonPathFunction> functionClass, List<Argument<?, ?>> args, Global global) {
        try {
            if (args.isEmpty()) {
                Constructor<JsonPathFunction> constructor = (Constructor<JsonPathFunction>) functionClass.getConstructor(Global.class);
                return constructor.newInstance(global);
            } else {
                Constructor<JsonPathFunction> constructor = (Constructor<JsonPathFunction>) functionClass.getConstructor(List.class, Global.class);
                return constructor.newInstance(args, global);
            }
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }
}
