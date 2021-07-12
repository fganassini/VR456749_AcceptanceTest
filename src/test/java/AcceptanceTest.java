import net.sourceforge.jwebunit.api.IElement;
import net.sourceforge.jwebunit.junit.WebTester;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AcceptanceTest {
    private WebTester tester;

    @Before
    public void prepare() {
        tester = new WebTester();
        tester.setBaseUrl("http://localhost:8080/");
    }

    @Test
    public void turnOnCollectorTest() {
        tester.beginAt("/");
        tester.assertTextPresent("Weather station simulator");
        tester.assertLinkPresentWithText("Turn on the collector");
        String e = tester.getElementAttributeByXPath("//input", "value");
        boolean status = Boolean.parseBoolean(e);
        Assert.assertFalse(status);
        tester.clickLinkWithText("Turn on the collector");
        tester.assertTextPresent("Collector function");
        e = tester.getElementAttributeByXPath("//input", "value");
        status = Boolean.parseBoolean(e);
        Assert.assertTrue(status);
    }

    @Test
    public void collectFunctionTest() {
        tester.beginAt("/");
        tester.clickLinkWithText("Turn on the collector");
        tester.assertLinkPresentWithText("Collect some weather data");
        tester.assertLinkPresentWithText("Read weather data");
        tester.assertLinkPresentWithText("Reconfigure the collector");
        tester.assertLinkPresentWithText("Turn off the collector");
    }

    @Test
    public void collectDataTest() {
        tester.beginAt("/");
        tester.clickLinkWithText("Turn on the collector");
        tester.clickLinkWithText("Collect some weather data");
        tester.assertTextPresent("Collection successfully done");
        tester.assertLinkPresentWithText("Go back");
        tester.clickLinkWithText("Go back");
        tester.assertTextPresent("Collector function");
    }

    @Test
    public void readDataTest() {
        tester.beginAt("/");
        tester.clickLinkWithText("Turn on the collector");
        tester.clickLinkWithText("Read weather data");
        Assert.assertTrue(tester.getElementsByXPath("//td").size() >= 0);
        tester.assertLinkPresentWithText("Go back");
        tester.clickLinkWithText("Go back");
        tester.assertTextPresent("Collector function");
    }

    @Test
    public void readDataWithCollectionTest() {
        tester.beginAt("/");
        tester.clickLinkWithText("Turn on the collector");
        tester.clickLinkWithText("Read weather data");
        int beforeSize = tester.getElementsByXPath("//td").size();
        tester.clickLinkWithText("Go back");
        tester.clickLinkWithText("Collect some weather data");
        tester.clickLinkWithText("Go back");
        tester.clickLinkWithText("Read weather data");
        int afterSize = tester.getElementsByXPath("//td").size();
        Assert.assertTrue(afterSize > beforeSize);
    }

    @Test
    public void readDataTwoTimesTest() {
        tester.beginAt("/");
        tester.clickLinkWithText("Turn on the collector");
        tester.clickLinkWithText("Read weather data");
        int beforeSize = tester.getElementsByXPath("//td").size();
        tester.clickLinkWithText("Go back");
        tester.clickLinkWithText("Read weather data");
        int afterSize = tester.getElementsByXPath("//td").size();
        Assert.assertEquals(afterSize, beforeSize);
    }

    @Test
    public void reconfigureTest() {
        tester.beginAt("/");
        tester.clickLinkWithText("Turn on the collector");
        tester.clickLinkWithText("Reconfigure the collector");
        tester.assertTextPresent("Reconfigure collector");
        tester.assertFormPresent();
        tester.assertSubmitButtonPresent();

        //Imposto una nuova configurazione

        tester.setTextField("therm1", "th1_2");
        tester.setTextField("therm2", "th2_2");
        tester.setTextField("barom", "b_2");
        tester.setTextField("windm", "w_2");
        tester.setTextField("rainm", "r_2");
        tester.setTextField("sunm", "s_2");
        tester.submit();

        System.out.println("Configuration submitted");

        tester.assertTextPresent("Reconfigure collector");
        tester.assertFormPresent();
        tester.assertSubmitButtonPresent();

        //Verifico che la configurazione sia stata applicata

        tester.assertTextFieldEquals("therm1", "th1_2");
        tester.assertTextFieldEquals("therm2", "th2_2");
        tester.assertTextFieldEquals("barom", "b_2");
        tester.assertTextFieldEquals("windm", "w_2");
        tester.assertTextFieldEquals("rainm", "r_2");
        tester.assertTextFieldEquals("sunm", "s_2");

        tester.assertLinkPresentWithText("Go back");
        tester.clickLinkWithText("Go back");
        tester.assertTextPresent("Collector function");
    }

    @Test
    public void reconfigureWithoutSubmitTest() {
        tester.beginAt("/");
        tester.clickLinkWithText("Turn on the collector");
        tester.clickLinkWithText("Reconfigure the collector");
        tester.assertTextPresent("Reconfigure collector");

        List<IElement> elist1 = tester.getElementsByXPath("//input");
        List<String> beforeList = new ArrayList<>();

        for(IElement e : elist1)
            beforeList.add(e.getAttribute("value"));

        tester.setTextField("therm1", "th1_3");
        tester.setTextField("therm2", "th2_3");
        tester.setTextField("barom", "b_3");
        tester.setTextField("windm", "w_3");
        tester.setTextField("rainm", "r_3");
        tester.setTextField("sunm", "s_3");

        tester.clickLinkWithText("Go back");
        tester.assertTextPresent("Collector function");
        tester.clickLinkWithText("Reconfigure the collector");

        //Verifico che la configurazione non sia stata applicata

        List<IElement> elist2 = tester.getElementsByXPath("//input");

        String before, after;
        for(int i = 0; i < (elist2.size()-1); i++) {
            before = beforeList.get(i);
            after = elist2.get(i).getAttribute("value");
            Assert.assertEquals(before, after);
        }

        tester.assertLinkPresentWithText("Go back");
        tester.clickLinkWithText("Go back");
        tester.assertTextPresent("Collector function");
    }

    @Test
    public void turnOffTest() {
        tester.beginAt("/");
        tester.clickLinkWithText("Turn on the collector");
        String e = tester.getElementAttributeByXPath("//input", "value");
        boolean status = Boolean.parseBoolean(e);
        Assert.assertTrue(status);
        tester.clickLinkWithText("Turn off the collector");
        tester.assertTextPresent("Weather station simulator");
        e = tester.getElementAttributeByXPath("//input", "value");
        status = Boolean.parseBoolean(e);
        Assert.assertFalse(status);
    }
}
