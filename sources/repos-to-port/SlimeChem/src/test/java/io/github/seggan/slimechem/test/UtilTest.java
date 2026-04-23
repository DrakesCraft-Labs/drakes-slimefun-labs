package com.github.drakescraft-labs.slimechem.test;

import io.github.addoncommunity.slimechem.implementation.atomic.Element;
import io.github.addoncommunity.slimechem.lists.Constants;
import io.github.addoncommunity.slimechem.utils.StringUtil;
import io.github.addoncommunity.slimechem.utils.Util;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UtilTest {

    @BeforeAll
    public static void setUp() {
        Constants.isTestingEnvironment = true;
    }

    @Test
    public void testResourceLoading() throws IOException {
        Asserts.assertEquals(
            3097,
            StringUtils.countMatches(StringUtil.getResourceAsString("isotopes.json"), "{")
        );
    }

    @Test
    public void testStringSplitting() {
        Asserts.assertEquals(
            StringUtil.splitString("2n"),
            new StringUtil.NumberAndString(2, "n")
        );

        Asserts.assertEquals(
            StringUtil.splitString("4He"),
            new StringUtil.NumberAndString(4, "He")
        );
    }

    @Test
    public void testListTrimming() {
        List<Element> list = new ArrayList<>(Arrays.asList(Element.values()));

        Util.trimList(list, 4);
        Asserts.assertEquals(list.size(), 4);

        Util.trimList(list, 10);
        Asserts.assertEquals(list.size(), 4);
    }
}
