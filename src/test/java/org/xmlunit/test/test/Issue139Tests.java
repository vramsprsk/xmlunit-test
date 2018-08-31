package org.xmlunit.test.test;

import org.junit.Test;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.ElementSelectors;
import org.xmlunit.matchers.CompareMatcher;

import static org.junit.Assert.assertThat;

//https://github.com/xmlunit/xmlunit/issues/139
public class Issue139Tests {

    @Test
    public void test() {
        String test1 = "" +
            "<PACKINGS>\n" +
            "    <PACKING>\n" +
            "        <TYPE>CCST</TYPE>\n" +
            "        <ORDERNUM>810000510</ORDERNUM>\n" +
            "        <SVCTAGS>\n" +
            "            <SVCTAG>\n" +
            "                <SVCTAGTYPE>DRAGON</SVCTAGTYPE>\n" +
            "                <SVCTAGNUMBER>768100005105001</SVCTAGNUMBER>\n" +
            "                <TIENUMBER>1</TIENUMBER>\n" +
            "                <BOXID>768100005105001</BOXID>\n" +
            "                <LENGTH>4</LENGTH>\n" +
            "                <WIDTH>5</WIDTH>\n" +
            "                <HEIGHT>10</HEIGHT>\n" +
            "                <PARTS>\n" +
            "                    <PART>\n" +
            "                        <PARTNUMBER>RKH5D</PARTNUMBER>\n" +
            "                        <PARTQTY>10</PARTQTY>\n" +
            "                    </PART>\n" +
            "                </PARTS>\n" +
            "            </SVCTAG>\n" +
            "            <SVCTAG>\n" +
            "                <SVCTAGTYPE>DRAGON</SVCTAGTYPE>\n" +
            "                <SVCTAGNUMBER>768100005105002</SVCTAGNUMBER>\n" +
            "                <TIENUMBER>2</TIENUMBER>\n" +
            "                <BOXID>768100005105002</BOXID>\n" +
            "                <LENGTH>4</LENGTH>\n" +
            "                <WIDTH>5</WIDTH>\n" +
            "                <HEIGHT>10</HEIGHT>\n" +
            "                <PARTS>\n" +
            "                    <PART>\n" +
            "                        <PARTNUMBER>FHMTN</PARTNUMBER>\n" +
            "                        <PARTQTY>10</PARTQTY>\n" +
            "                    </PART>\n" +
            "                </PARTS>\n" +
            "            </SVCTAG>\n" +
            "        </SVCTAGS>\n" +
            "    </PACKING>\n" +
            "</PACKINGS>";

        String test2 = "" +
            "<PACKINGS>\n" +
            "    <PACKING>\n" +
            "        <TYPE>CCST</TYPE>\n" +
            "        <ORDERNUM>810000510</ORDERNUM>\n" +
            "        <SVCTAGS>\n" +
            "            <SVCTAG>\n" +
            "                <SVCTAGTYPE>DRAGON</SVCTAGTYPE>\n" +
            "                <SVCTAGNUMBER>768100005105002</SVCTAGNUMBER>\n" +
            "                <TIENUMBER>2</TIENUMBER>\n" +
            "                <BOXID>768100005105002</BOXID>\n" +
            "                <LENGTH>4</LENGTH>\n" +
            "                <WIDTH>5</WIDTH>\n" +
            "                <HEIGHT>10</HEIGHT>\n" +
            "                <PARTS>\n" +
            "                    <PART>\n" +
            "                        <PARTNUMBER>FHMTN</PARTNUMBER>\n" +
            "                        <PARTQTY>10</PARTQTY>\n" +
            "                    </PART>\n" +
            "                </PARTS>\n" +
            "            </SVCTAG>\n" +
            "            <SVCTAG>\n" +
            "                <SVCTAGTYPE>DRAGON</SVCTAGTYPE>\n" +
            "                <SVCTAGNUMBER>768100005105001</SVCTAGNUMBER>\n" +
            "                <TIENUMBER>1</TIENUMBER>\n" +
            "                <BOXID>768100005105001</BOXID>\n" +
            "                <LENGTH>4</LENGTH>\n" +
            "                <WIDTH>5</WIDTH>\n" +
            "                <HEIGHT>10</HEIGHT>\n" +
            "                <PARTS>\n" +
            "                    <PART>\n" +
            "                        <PARTNUMBER>RKH5D</PARTNUMBER>\n" +
            "                        <PARTQTY>10</PARTQTY>\n" +
            "                    </PART>\n" +
            "                </PARTS>\n" +
            "            </SVCTAG>\n" +
            "        </SVCTAGS>\n" +
            "    </PACKING>\n" +
            "</PACKINGS>";

        assertThat(test1, CompareMatcher.isSimilarTo(test2)
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
}
