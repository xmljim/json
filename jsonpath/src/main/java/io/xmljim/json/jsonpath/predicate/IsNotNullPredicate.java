package io.xmljim.json.jsonpath.predicate;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.filter.Accessor;
import io.xmljim.json.jsonpath.filter.Filter;
import io.xmljim.json.jsonpath.filter.FilterType;
import io.xmljim.json.jsonpath.predicate.expression.PathExpression;
import io.xmljim.json.jsonpath.predicate.expression.PredicateExpression;

import java.util.List;

public class IsNotNullPredicate extends AbstractFilterPredicate {
    public IsNotNullPredicate(PredicateExpression leftSide, PredicateExpression rightSide) {
        super(leftSide, rightSide, PredicateOperator.IS_NOT_NULL);
    }

    @Override
    public boolean test(Context context) {
        if (leftSide() instanceof PathExpression pathExpression) {
            Filter lastFilter = pathExpression.getLastFilter();
            if (lastFilter.getOperatorType() == FilterType.CHILD) {
                Accessor accessor = Accessor.parse(lastFilter.getExpression());
                List<Context> resultList = pathExpression.subfilter(context).toList();

                if (resultList.isEmpty()) {
                    return false;
                } else if (resultList.size() == 1) {//this is what we want, one result
                    return switch (resultList.get(0).type()) {
                        case ARRAY -> resultList.get(0).get().asJsonArray().getOptional(accessor.get()).orElse(null) != null;
                        case OBJECT -> resultList.get(0).get().asJsonObject().getOptional(accessor.get()).orElse(null) != null;
                        default -> resultList.get(0).get().asJsonValue().get() != null;
                    };
                } else {
                    return false;
                }

            } else {
                return leftSide().getValue(context).orElse(null) != null;
            }
        } else {
            return leftSide().getValue(context).orElse(null) != null;
        }
    }
}
