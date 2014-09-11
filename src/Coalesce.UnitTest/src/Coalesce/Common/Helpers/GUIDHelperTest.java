package Coalesce.Common.Helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.UUID;

import org.junit.Test;

/*-----------------------------------------------------------------------------'
 Copyright 2014 - InCadence Strategic Solutions Inc., All Rights Reserved

 Notwithstanding any contractor copyright notice, the Government has Unlimited
 Rights in this work as defined by DFARS 252.227-7013 and 252.227-7014.  Use
 of this work other than as specifically authorized by these DFARS Clauses may
 violate Government rights in this work.

 DFARS Clause reference: 252.227-7013 (a)(16) and 252.227-7014 (a)(16)
 Unlimited Rights. The Government has the right to use, modify, reproduce,
 perform, display, release or disclose this computer software and to have or
 authorize others to do so.

 Distribution Statement D. Distribution authorized to the Department of
 Defense and U.S. DoD contractors only in support of U.S. DoD efforts.
 -----------------------------------------------------------------------------*/

public class GUIDHelperTest {

    private static final String UPPERCASE_NO_BRACKETS = "487568C1-B306-4B23-A94E-456E6AC3C113";
    private static final String UPPERCASE_WITH_BRACKETS = "{487568C1-B306-4B23-A94E-456E6AC3C113}";
    private static final String LOWERCASE_NO_BRACKETS = "487568c1-b306-4b23-a94e-456e6ac3c113";
    private static final String LOWERCASE_WITH_BRACKETS = "{487568c1-b306-4b23-a94e-456e6ac3c113}";

    private static final String UPPERCASE_MISSING_OPENING_BRACKET = "487568C1-B306-4B23-A94E-456E6AC3C113}";
    private static final String UPPERCASE_MISSING_CLOSING_BRACKET = "{487568C1-B306-4B23-A94E-456E6AC3C113";
    private static final String UPPERCASE_NO_BRACKETS_SHORT = "487568C1-B306-4B23-A94E-456E6AC3C13";
    private static final String UPPERCASE_NO_BRACKETS_LONG  = "487568C1-B306-4B23-A94E-456E6AC3C1113";
    private static final String UPPERCASE_WITH_BRACKETS_WRONG_LETTER = "{487568C1-Z306-4B23-A94E-456E6AC3C113}";
    private static final String UPPERCASE_NO_BRACKETS_WRONG_LETTER = "487568C1-Z306-4B23-A94E-456E6AC3C13";
    private static final String UPPERCASE_NO_BRACKETS_SPECIAL_CHAR = "487568C1-!306-4B23-A94E-456E6AC3C13";

    /*
     * @BeforeClass public static void setUpBeforeClass() throws Exception { }
     * 
     * @AfterClass public static void tearDownAfterClass() throws Exception { }
     * 
     * @Before public void setUp() throws Exception { }
     * 
     * @After public void tearDown() throws Exception { }
     */

    @Test
    public void IsValidTest()
    {
        assertTrue(GUIDHelper.IsValid(UUID.randomUUID().toString().toLowerCase()));
    }

    @Test
    public void IsValidLowerCaseWithBracketsTest()
    {
        assertTrue(GUIDHelper.IsValid(LOWERCASE_WITH_BRACKETS));
    }

    @Test
    public void IsValidLowerCaseWithoutBracketsTest()
    {
        assertTrue(GUIDHelper.IsValid(LOWERCASE_NO_BRACKETS));
    }

    @Test
    public void IsValidUpperCaseWithBracketsTest()
    {
        assertTrue(GUIDHelper.IsValid(UPPERCASE_WITH_BRACKETS));
    }

    @Test
    public void IsValidUpperCaseWithoutBracketsTest()
    {
        assertTrue(GUIDHelper.IsValid(UPPERCASE_NO_BRACKETS));
    }

    @Test
    public void IsValidUpperCaseNullTest()
    {
        assertFalse(GUIDHelper.IsValid(null));
    }

    @Test
    public void IsValidUpperCaseEmptyTest()
    {
        assertFalse(GUIDHelper.IsValid(""));
    }

    @Test
    public void IsValidUpperCaseWhitespaceTest()
    {
        assertFalse(GUIDHelper.IsValid(" "));
    }

    @Test
    public void IsValidUpperCaseStringTest()
    {
        assertFalse(GUIDHelper.IsValid("String"));
    }

    @Test
    public void IsValidInvalidShortTest()
    {
        assertFalse(GUIDHelper.IsValid(UPPERCASE_NO_BRACKETS_SHORT));
    }

    @Test
    public void IsValidInvalidLongTest()
    {
        assertFalse(GUIDHelper.IsValid(UPPERCASE_NO_BRACKETS_LONG));
    }

    @Test
    public void IsValidInvalidWrongLeterTest()
    {
        assertFalse(GUIDHelper.IsValid(UPPERCASE_NO_BRACKETS_WRONG_LETTER));
    }

    @Test
    public void IsValidInvalidSpecialCharacterTest()
    {
        assertFalse(GUIDHelper.IsValid(UPPERCASE_NO_BRACKETS_SPECIAL_CHAR));
    }

    @Test
    public void IsValidInvalidMissingOpenningBracketTest()
    {
        assertFalse(GUIDHelper.IsValid(UPPERCASE_MISSING_OPENING_BRACKET));
    }

    @Test
    public void IsValidInvalidMissingClosingBracketTest()
    {
        assertFalse(GUIDHelper.IsValid(UPPERCASE_MISSING_CLOSING_BRACKET));
    }

    @Test
    public void HasBracketsTest()
    {
        assertFalse(GUIDHelper.HasBrackets(UUID.randomUUID().toString().toLowerCase()));
    }

    @Test
    public void HasBracketsLowerCaseWithBracketsTest()
    {
        assertTrue(GUIDHelper.HasBrackets(LOWERCASE_WITH_BRACKETS));
    }

    @Test
    public void HasBracketsLowerCaseWithoutBracketsTest()
    {
        assertFalse(GUIDHelper.HasBrackets(LOWERCASE_NO_BRACKETS));
    }

    @Test
    public void HasBracketsUpperCaseWithBracketsTest()
    {
        assertTrue(GUIDHelper.HasBrackets(UPPERCASE_WITH_BRACKETS));
    }

    @Test
    public void HasBracketsUpperCaseWithoutBracketsTest()
    {
        assertFalse(GUIDHelper.HasBrackets(UPPERCASE_NO_BRACKETS));
    }

    @Test
    public void HasBracketsInvalidShortTest()
    {
        assertFalse(GUIDHelper.HasBrackets(UPPERCASE_NO_BRACKETS_SHORT));
    }

    @Test
    public void HasBracketsInvalidLongTest()
    {
        assertFalse(GUIDHelper.HasBrackets(UPPERCASE_NO_BRACKETS_LONG));
    }

    @Test
    public void HasBracketsInvalidWithBracketsTest()
    {
        assertFalse(GUIDHelper.HasBrackets(UPPERCASE_WITH_BRACKETS_WRONG_LETTER));
    }

    @Test
    public void HasBracketsInvalidWithoutBracketsTest()
    {
        assertFalse(GUIDHelper.HasBrackets(UPPERCASE_NO_BRACKETS_WRONG_LETTER));
    }

    @Test
    public void HasBracketsInvalidMissingOpeningBracketTest()
    {
        assertFalse(GUIDHelper.HasBrackets(UPPERCASE_MISSING_OPENING_BRACKET));
    }

    @Test
    public void HasBracketsInvalidMissingClosingBracketTest()
    {
        assertFalse(GUIDHelper.HasBrackets(UPPERCASE_MISSING_CLOSING_BRACKET));
    }

    @Test
    public void AddBracketsTest()
    {

        String uuid = UUID.randomUUID().toString().toLowerCase();

        String value = GUIDHelper.AddBrackets(uuid);

        assertEquals("{" + uuid.toUpperCase() + "}", value);
    }

    @Test
    public void AddBracketsLowerCaseWithBracketsTest()
    {

        String value = GUIDHelper.AddBrackets(LOWERCASE_WITH_BRACKETS);

        assertEquals(UPPERCASE_WITH_BRACKETS, value);
    }

    @Test
    public void AddBracketsLowerCaseWithoutBracketsTest()
    {

        String value = GUIDHelper.AddBrackets(LOWERCASE_NO_BRACKETS);

        assertEquals(UPPERCASE_WITH_BRACKETS, value);
    }

    @Test
    public void AddBracketsUpperCaseWithBracketsTest()
    {

        String value = GUIDHelper.AddBrackets(UPPERCASE_WITH_BRACKETS);

        assertEquals(UPPERCASE_WITH_BRACKETS, value);
    }

    @Test
    public void AddBracketsUpperCaseWithoutBracketsTest()
    {

        String value = GUIDHelper.AddBrackets(UPPERCASE_NO_BRACKETS);

        assertEquals(UPPERCASE_WITH_BRACKETS, value);
    }

    @Test
    public void AddBracketsInvalidShortTest()
    {

        String validUuid = GUIDHelper.AddBrackets(UPPERCASE_NO_BRACKETS_SHORT);

        assertNull(validUuid);
    }

    @Test
    public void AddBracketsInvalidLongTest()
    {

        String validUuid = GUIDHelper.AddBrackets(UPPERCASE_NO_BRACKETS_LONG);

        assertNull(validUuid);
    }

    @Test
    public void AddBracketsInvalidWithBracketsTest()
    {

        String value = GUIDHelper.AddBrackets(UPPERCASE_WITH_BRACKETS_WRONG_LETTER);

        assertNull(value);
    }

    @Test
    public void AddBracketsInvalidWithoutBracketsTest()
    {

        String value = GUIDHelper.AddBrackets(UPPERCASE_NO_BRACKETS_WRONG_LETTER);

        assertNull(value);
    }

    @Test
    public void RemoveBracketsTest()
    {

        String uuid = UUID.randomUUID().toString().toLowerCase();

        String value = GUIDHelper.RemoveBrackets("{" + uuid + "}");

        assertEquals(uuid.toUpperCase(), value);
    }

    @Test
    public void RemoveBracketsLowerCaseWithBracketsTest()
    {

        String value = GUIDHelper.RemoveBrackets(LOWERCASE_WITH_BRACKETS);

        assertTrue(UPPERCASE_NO_BRACKETS.equals(value));
    }

    @Test
    public void RemoveBracketsLowerCaseWithoutBracketsTest()
    {

        String value = GUIDHelper.RemoveBrackets(LOWERCASE_NO_BRACKETS);

        assertTrue(UPPERCASE_NO_BRACKETS.equals(value));
    }

    @Test
    public void RemoveBracketsUpperCaseWithBracketsTest()
    {

        String value = GUIDHelper.RemoveBrackets(UPPERCASE_WITH_BRACKETS);

        assertTrue(UPPERCASE_NO_BRACKETS.equals(value));
    }

    @Test
    public void RemoveBracketsUpperCaseWithoutBracketsTest()
    {

        String value = GUIDHelper.RemoveBrackets(UPPERCASE_NO_BRACKETS);

        assertTrue(UPPERCASE_NO_BRACKETS.equals(value));
    }

    @Test
    public void RemoveBracketsInvalidShortTest()
    {

        String validUuid = GUIDHelper.RemoveBrackets(UPPERCASE_NO_BRACKETS_SHORT);

        assertNull(validUuid);
    }

    @Test
    public void RemoveBracketsInvalidLongTest()
    {

        String validUuid = GUIDHelper.RemoveBrackets(UPPERCASE_NO_BRACKETS_LONG);

        assertNull(validUuid);
    }

    @Test
    public void RemoveBracketsInvalidWithBracketsTest()
    {

        String value = GUIDHelper.RemoveBrackets(UPPERCASE_WITH_BRACKETS_WRONG_LETTER);

        assertNull(value);
    }

    @Test
    public void RemoveBracketsInvalidWithoutBracketsTest()
    {

        String value = GUIDHelper.RemoveBrackets(UPPERCASE_NO_BRACKETS_WRONG_LETTER);

        assertNull(value);
    }

    @Test
    public void GetGuidLowerCaseWithBracketsTest()
    {

        UUID guid = GUIDHelper.GetGuid(LOWERCASE_WITH_BRACKETS);

        assertEquals(guid.toString(), LOWERCASE_NO_BRACKETS);
    }

    @Test
    public void GetGuidLowerCaseWithoutBracketsTest()
    {

        UUID guid = GUIDHelper.GetGuid(LOWERCASE_NO_BRACKETS);

        assertEquals(guid.toString(), LOWERCASE_NO_BRACKETS);
    }

    @Test
    public void GetGuidUpperCaseWithBracketsTest()
    {

        UUID guid = GUIDHelper.GetGuid(UPPERCASE_WITH_BRACKETS);

        assertEquals(guid.toString(), LOWERCASE_NO_BRACKETS);
    }

    @Test
    public void GetGuidUpperCaseWithoutBracketsTest()
    {

        UUID guid = GUIDHelper.GetGuid(UPPERCASE_NO_BRACKETS);

        assertEquals(guid.toString(), LOWERCASE_NO_BRACKETS);
    }

    @Test
    public void GetGuidInvalidShortTest()
    {

        UUID guid = GUIDHelper.GetGuid(UPPERCASE_NO_BRACKETS_SHORT);

        assertNull(guid);
    }

    @Test
    public void GetGuidInvalidLongTest()
    {

        UUID guid = GUIDHelper.GetGuid(UPPERCASE_NO_BRACKETS_LONG);

        assertNull(guid);
    }

    @Test
    public void GetGuidInvalidWithBracketsTest()
    {

        UUID guid = GUIDHelper.GetGuid(UPPERCASE_WITH_BRACKETS_WRONG_LETTER);

        assertNull(guid);
    }

    @Test
    public void GetGuidInvalidWithoutBracketsTest()
    {

        UUID guid = GUIDHelper.GetGuid(UPPERCASE_NO_BRACKETS_WRONG_LETTER);

        assertNull(guid);
    }

    @Test
    public void GetGuidStringTest()
    {

        UUID guid = GUIDHelper.GetGuid(UPPERCASE_NO_BRACKETS);

        String guidString = GUIDHelper.GetGuidString(guid);

        assertEquals(UPPERCASE_NO_BRACKETS, guidString);
    }

    @Test
    public void GetGuidStringNullTest()
    {

        String guidString = GUIDHelper.GetGuidString(null);

        assertNull(guidString);
    }

    @Test
    public void GetGuidStringWithBracketsFalseTest()
    {

        UUID guid = GUIDHelper.GetGuid(UPPERCASE_NO_BRACKETS);

        String guidString = GUIDHelper.GetGuidString(guid, false);

        assertEquals(UPPERCASE_NO_BRACKETS, guidString);
    }

    @Test
    public void GetGuidStringWithBracketsFalseNullTest()
    {

        String guidString = null;

        try
        {

            guidString = GUIDHelper.GetGuidString(null, false);
            assertNull(guidString);
        }
        catch (Exception ex)
        {
            fail(ex.getMessage());
        }

    }

    @Test
    public void GetGuidStringWithBracketsTrueTest()
    {

        UUID guid = GUIDHelper.GetGuid(UPPERCASE_NO_BRACKETS);

        String guidString = GUIDHelper.GetGuidString(guid, true);

        assertEquals(UPPERCASE_WITH_BRACKETS, guidString);
    }

    @Test
    public void GetGuidStringWithBracketsTrueNullTest()
    {

        String guidString = GUIDHelper.GetGuidString(null, true);

        assertNull(guidString);
    }

}
