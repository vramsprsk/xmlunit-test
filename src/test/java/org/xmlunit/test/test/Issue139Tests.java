package org.xmlunit.test.test;

import org.junit.BeforeClass;
import org.junit.Test;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.ComparisonControllers;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.ElementSelectors;
import org.xmlunit.matchers.CompareMatcher;
import org.xmlunit.test.test.data.TestData;
import org.xmlunit.test.test.data.TestDataLoader;

import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

//https://github.com/xmlunit/xmlunit/issues/139
public class Issue139Tests {

    private static Map<String, TestData> allTestData;

    @BeforeClass
    public static void setUp() {
        allTestData = TestDataLoader.load("issue139.properties");
    }

    @Test
    public void test() {

        TestData data = allTestData.get("test1");

        assertThat(data.testXml, CompareMatcher.isSimilarTo(data.testXml)
            .ignoreWhitespace()
            .normalizeWhitespace()
            .withNodeMatcher(new DefaultNodeMatcher(
                ElementSelectors.conditionalBuilder()
                    .whenElementIsNamed("SVCTAG")
                    .thenUse(ElementSelectors.byXPath("./SVCTAGNUMBER", ElementSelectors.byNameAndText))
                    .elseUse(ElementSelectors.byName)
                    .build()
            )));
    }

    @Test
    public void testUsingDiffBuilder() {

        TestData data = allTestData.get("test1");

        Diff diff =  DiffBuilder.compare(data.controlXml).withTest(data.testXml)
            .ignoreWhitespace()
            .normalizeWhitespace()
            .withNodeMatcher(new DefaultNodeMatcher(
                ElementSelectors.conditionalBuilder()
                    .whenElementIsNamed("SVCTAG")
                    .thenUse(ElementSelectors.byXPath("./SVCTAGNUMBER", ElementSelectors.byNameAndText))
                    .elseUse(ElementSelectors.byName)
                    .build()
            ))
            .withComparisonController(ComparisonControllers.StopWhenSimilar)
            .checkForSimilar()
            .build();

        boolean hasDifferences = diff.hasDifferences();

        assertThat(hasDifferences, is(equalTo(false)));
    }

}
