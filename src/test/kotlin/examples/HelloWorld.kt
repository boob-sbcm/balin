/******************************************************************************
 * Copyright 2016 Edinson E. Padrón Urdaneta
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/

/******************************************************************************
 * You may need to provide the path for Firefox as well as for the gecko-driver
 * is you wish to run the test.
 *
 * I tried using the HtmlUnitDriver but its behaviour was quite unexpected.
 *****************************************************************************/

/* ***************************************************************************/
import com.github.epadronu.balin.core.Browser
import com.github.epadronu.balin.core.Page
import com.github.epadronu.balin.extensions.`$`
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable
import org.openqa.selenium.support.ui.ExpectedConditions.textToBe
import org.testng.Assert
import org.testng.annotations.Test
/* ***************************************************************************/

/* ***************************************************************************/
class IndexPage(browser: Browser) : Page(browser) {

    override val url = "http://kotlinlang.org/"

    override val at = at {
        title == "Kotlin Programming Language"
    }

    private val tryItButton by lazy {
        waitFor { elementToBeClickable(By.className("get-kotlin-button")) }
    }

    fun goToTryItPage(): TryItPage = tryItButton.click(::TryItPage)
}

class TryItPage(browser: Browser) : Page(browser) {
    override val at = at {
        title == "Simplest version | Try Kotlin"
    }

    private val consoleOutput by lazy {
        `$`(".standard-output", 0)
    }

    private val runButton by lazy {
        waitFor { elementToBeClickable(By.id("runButton")) }
    }

    fun runHelloWorld(): String {
        runButton.click()

        waitFor { textToBe(By.cssSelector(".standard-output"), "Hello, world!") }

        return consoleOutput.text
    }
}
/* ***************************************************************************/

/* ***************************************************************************/
class HelloWorldTest {

    @Test
    fun `Test the HelloWorld example`() {
        Browser.drive {
            val indexPage = to(::IndexPage)

            val tryItPage = indexPage.goToTryItPage()

            Assert.assertEquals(tryItPage.runHelloWorld(), "Hello, world!")
        }
    }
}
/* ***************************************************************************/
