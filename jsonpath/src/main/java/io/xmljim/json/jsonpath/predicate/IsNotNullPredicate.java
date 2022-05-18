package io.xmljim.json.jsonpath.predicate;

import io.xmljim.json.jsonpath.context.Context;
import io.xmljim.json.jsonpath.filter.Accessor;
import io.xmljim.json.jsonpath.filter.Filter;
import io.xmljim.json.jsonpath.filter.FilterType;
import io.xmljim.json.jsonpath.predicate.expression.Expression;
import io.xmljim.json.jsonpath.predicate.expression.FilterExpression;

import java.util.List;

public class IsNotNullPredicate extends AbstractFilterPredicate {
    public IsNotNullPredicate(Expression leftSide, Expression rightSide) {
        super(leftSide, rightSide, PredicateOperator.IS_NOT_NULL);
    }

    @Override
    public boolean test(Context context) {
        if (leftSide() instanceof FilterExpression pathExpression) {
            Filter lastFilter = pathExpression.getLastFilter();
            if (lastFilter.getFilterType() == FilterType.CHILD) {
                Accessor accessor = Accessor.parse(lastFilter.getExpression());
                List<Context> resultList = pathExpression.subfilter(context).toList();

                if (resultList.isEmpty()) {
                    return false;
                } else if (resultList.size() == 1) {//this is what we want, one result
                    return switch (resultList.get(0).type()) {
                        case ARRAY -> resultList.get(0).array().getOptional(accessor.get()).orElse(null) != null;
                        case OBJECT -> resultList.get(0).object().getOptional(accessor.get()).orElse(null) != null;
                        default -> resultList.get(0).value() != null;
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
