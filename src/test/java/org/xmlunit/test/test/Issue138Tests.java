package org.xmlunit.test.test;

import org.junit.Test;
import org.w3c.dom.Attr;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.ComparisonControllers;
import org.xmlunit.diff.ComparisonType;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.Difference;
import org.xmlunit.diff.DifferenceEvaluator;
import org.xmlunit.diff.ElementSelector;
import org.xmlunit.diff.ElementSelectors;
import org.xmlunit.util.Predicate;

import static org.xmlunit.diff.DifferenceEvaluators.Default;
import static org.xmlunit.diff.DifferenceEvaluators.chain;
import static org.xmlunit.diff.DifferenceEvaluators.downgradeDifferencesToEqual;

//https://github.com/xmlunit/xmlunit/issues/138
public class Issue138Tests {

    @Test
    public void tets() {

        //@formatter:off

        String control = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> " +
            "<ns11:Requests xmlns:ns11=\"/data/Requests/10.0\" xmlns:ns5=\"/data/Headers/10.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"\"> " +
                "<ns5:headers/> " +
                "<ns11:state>Complete</ns11:state> " +
                "<ns11:pID>b279f6-1bf3</ns11:pID> " +
                "<ns11:community>BEST</ns11:community> " +
                "<ns11:eID>5991093</ns11:eID> " +
                "<ns11:eType>OnOfferActivation</ns11:eType> " +
                "<ns11:Request> " +
                    "<ns11:type>Notify</ns11:type> " +
                    "<ns11:requestProperties ns11:name=\"Channel\" ns11:value=\"mobile\"/> " +
                    "<ns11:requestProperties ns11:name=\"ClientName\" ns11:value=\"BEST\"/> " +
                    "<ns11:requestProperties ns11:name=\"Activation.ReferenceId\" ns11:value=\"5b8b-4522\"/> " +
                    "<ns11:requestProperties ns11:name=\"ActionCID\" ns11:value=\"9f6b-4619\"/> " +
                "</ns11:Request> " +
            "</ns11:Requests>";

        String test = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?> " +
            "<ns4:Requests xmlns:ns2=\"/data/Headers/1.0\" xmlns:ns4=\"/data/Requests/10.0\" > " +
                "<ns2:headers/> " +
                "<ns4:state>Complete</ns4:state> " +
                "<ns4:pID>b279f6-1bf3</ns4:pID> " +
                "<ns4:community>BEST</ns4:community> " +
                "<ns4:eID>5991093</ns4:eID> " +
                "<ns4:eType>OnOfferActivation</ns4:eType> " +
                "<ns4:Request> " +
                    "<ns4:type>Notify</ns4:type> " +
                    "<ns4:requestProperties ns4:name=\"Channel\" ns4:value=\"mobile\"/> " +
                    "<ns4:requestProperties ns4:name=\"MID\" ns4:value=\"599553\"/> " +
                    "<ns4:requestProperties ns4:name=\"ClientName\" ns4:value=\"BEST\"/> " +
                    "<ns4:requestProperties ns4:name=\"Activation.ReferenceId\" ns4:value=\"5b8b-4522\"/> " +
                    "<ns4:requestProperties ns4:name=\"Attibutes\" ns4:value=\"vjs,null,null,2018-08-17 11:21:06.827\"/> " +
                    "<ns4:requestProperties ns4:name=\"ActionCID\" ns4:value=\"8661-42b0\"/> " +
                "</ns4:Request> " +
            "</ns4:Requests>";


        //@formatter:on

        //remove some noises
        DifferenceEvaluator de = chain(Default,
            downgradeDifferencesToEqual(ComparisonType.XML_STANDALONE),
            downgradeDifferencesToEqual(ComparisonType.NAMESPACE_PREFIX),
            downgradeDifferencesToEqual(ComparisonType.SCHEMA_LOCATION),
            downgradeDifferencesToEqual(ComparisonType.CHILD_NODELIST_SEQUENCE)
        );

        ElementSelector elementSelector = ElementSelectors.conditionalBuilder()
            .whenElementIsNamed("requestProperties")
            .thenUse(ElementSelectors.byNameAndAttributesControlNS("name"))
            .elseUse(ElementSelectors.byName)
            .build();

        Diff diff = DiffBuilder.compare(control)
            .withTest(test)
            .ignoreElementContentWhitespace()
            .withDifferenceEvaluator(de)
            .withComparisonController(ComparisonControllers.Default)
            .withNodeMatcher(new DefaultNodeMatcher(elementSelector))
            .build();

        for (Difference difference : diff.getDifferences()) {
            System.out.println(difference.toString());
        }
    }

    private final class AttributeFilter implements Predicate<Attr> {

        private final String attrName;

        private AttributeFilter(String attrName) {
            this.attrName = attrName;
        }

        @Override
        public boolean test(Attr toTest) {
            return !attrName.equalsIgnoreCase(toTest.getName());
        }
    }
}
