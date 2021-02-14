package Utils.Enums.Data.TestData;

import Utils.Enums.Base.PagesCollections;
import com.amazonaws.waiters.Waiter;
import org.openqa.selenium.WebDriver;

public interface MainReferences {
    default ThreadLocal<WebDriver> getDriver() {
        return getStaticDriver();
    }

    default PagesCollections getPages() {
        return getStaticPages();
    }

    default AssertionActions getActions() {
        return getStaticActions();
    }

    default Waiter getWaiter() {
        return getStaticWaiter();
    }

    default Waiter getWaiter(long seconds) {
        return getStaticWaiter(seconds);
    }

    static PagesCollections getStaticPages() {
        return ReferencesProvider.getReferenceObj().getPageCollection();
    }

    static ThreadLocal<WebDriver> getStaticDriver() {
        return ReferencesProvider.getDriver();
    }

    static AssertionActions getStaticActions() {
        return ReferencesProvider.getReferenceObj().getActions();
    }

    static Waiter getStaticWaiter() {
        return ReferencesProvider.getWaiter();
    }

    static Waiter getStaticWaiter(long seconds) {
        return ReferencesProvider.getWaiter(seconds);
    }
}
