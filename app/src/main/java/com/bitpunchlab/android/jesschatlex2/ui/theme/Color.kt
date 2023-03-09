package com.bitpunchlab.android.jesschatlex2.ui.theme

import androidx.compose.ui.graphics.Color
import com.bitpunchlab.android.jesschatlex2.helpers.ColorMode
import com.bitpunchlab.android.jesschatlex2.helpers.Element

object JessChatLex {

    val lightBlue = HashMap<Element, Color>()
    val lightGreen = HashMap<Element, Color>()
    val lightBrown = HashMap<Element, Color>()
    val lightPurple = HashMap<Element, Color>()
    val darkBlue = HashMap<Element, Color>()
    val darkGreen = HashMap<Element, Color>()
    val darkBrown = HashMap<Element, Color>()
    val darkPurple = HashMap<Element, Color>()
    val colorScheme = HashMap<ColorMode, HashMap<Element, Color>>()
    val light = HashMap<Element, Color>()
    val dark = HashMap<Element, Color>()

    init {
        setupColor()
    }

    fun setupColor() {

        lightBlue.put(Element.BANNER, Color(0xFF2677e0))
        lightBlue.put(Element.TEXT, Color(0xFF0b29bd))
        lightBlue.put(Element.BACKGROUND, Color(0xFFabe2ed))
        lightBlue.put(Element.FIELD_BACKGROUND, Color.White)
        lightBlue.put(Element.FIELD_BORDER, Color(0xFF2677e0))
        lightBlue.put(Element.BUTTON_COLOR, Color(0xFF2677e0))
        // Color(0xFF2677e0)
        lightBlue.put(Element.BUTTON_BORDER, Color.Transparent)
        lightBlue.put(Element.BUTTON_TEXT, Color.White)
        lightBlue.put(Element.BUTTON_BACKGROUND, Color(0xFFabe2ed))
        lightBlue.put(Element.CLICKABLE, Color(0xFF2677e0))
        lightBlue.put(Element.ERROR_TEXT, Color.Red)
        lightBlue.put(Element.OTHER_TEXT, Color.Black)

        lightGreen.put(Element.BANNER, Color(0xFF0c7829))
        lightGreen.put(Element.TEXT, Color(0xFF046b23))
        lightGreen.put(Element.BACKGROUND, Color(0xFFb3f5c7))
        lightGreen.put(Element.FIELD_BACKGROUND, Color.White)
        lightGreen.put(Element.FIELD_BORDER, Color(0xFF0c7829))
        lightGreen.put(Element.BUTTON_COLOR, Color(0xFF0c7829))
        lightGreen.put(Element.BUTTON_BORDER, Color.Transparent)
        lightGreen.put(Element.BUTTON_TEXT, Color.White)
        lightGreen.put(Element.BUTTON_BACKGROUND, Color(0xFFb3f5c7))
        lightGreen.put(Element.CLICKABLE, Color(0xFF0c7829))
        lightGreen.put(Element.ERROR_TEXT, Color.Red)
        lightGreen.put(Element.OTHER_TEXT, Color.Black)

        lightBrown.put(Element.BANNER, Color(0xFF826004))
        lightBrown.put(Element.TEXT, Color(0xFF704903))
        lightBrown.put(Element.BACKGROUND, Color(0xFFf0cd90))
        lightBrown.put(Element.FIELD_BACKGROUND, Color.White)
        lightBrown.put(Element.FIELD_BORDER, Color(0xFF826004))
        lightBrown.put(Element.BUTTON_COLOR, Color(0xFF826004))
        lightBrown.put(Element.BUTTON_BORDER, Color.Transparent)
        lightBrown.put(Element.BUTTON_TEXT, Color.White)
        lightBrown.put(Element.BUTTON_BACKGROUND, Color(0xFFf0cd90))
        lightBrown.put(Element.CLICKABLE, Color(0xFF826004))
        lightBrown.put(Element.ERROR_TEXT, Color.Red)
        lightBrown.put(Element.OTHER_TEXT, Color.Black)

        lightPurple.put(Element.BANNER, Color(0xFF570ad1))
        lightPurple.put(Element.TEXT, Color(0xFF500880))
        lightPurple.put(Element.BACKGROUND, Color(0xFFbc9bfa))
        lightPurple.put(Element.FIELD_BACKGROUND, Color.White)
        lightPurple.put(Element.FIELD_BORDER, Color(0xFF570ad1))
        lightPurple.put(Element.BUTTON_COLOR, Color(0xFF570ad1))
        lightPurple.put(Element.BUTTON_BORDER, Color.Transparent)
        lightPurple.put(Element.BUTTON_TEXT, Color.White)
        lightPurple.put(Element.BUTTON_BACKGROUND, Color(0xFFbc9bfa))
        lightPurple.put(Element.CLICKABLE, Color(0xFF570ad1))
        lightPurple.put(Element.ERROR_TEXT, Color.Red)
        lightPurple.put(Element.OTHER_TEXT, Color.Black)

        darkBlue.put(Element.BANNER, Color(0xFF032e73))
        darkBlue.put(Element.TEXT, Color(0xFF94d0f2))
        darkBlue.put(Element.BACKGROUND, Color(0xFF032e73))
        darkBlue.put(Element.FIELD_BACKGROUND, Color.Transparent)
        darkBlue.put(Element.FIELD_BORDER, Color(0xFF94d0f2))
        darkBlue.put(Element.BUTTON_COLOR, Color.Transparent)
        darkBlue.put(Element.BUTTON_BORDER, Color(0xFF94d0f2))
        darkBlue.put(Element.BUTTON_TEXT, Color.White)
        darkBlue.put(Element.BUTTON_BACKGROUND, Color.Transparent)
        darkBlue.put(Element.CLICKABLE, Color.Yellow)
        darkBlue.put(Element.ERROR_TEXT, Color(0xFFe897e7))
        darkBlue.put(Element.OTHER_TEXT, Color(0xFF2677e0))

        darkGreen.put(Element.BANNER, Color(0xFF04470e))
        darkGreen.put(Element.TEXT, Color(0xFFb3f5c7))
        darkGreen.put(Element.BACKGROUND, Color(0xFF04470e))
        darkGreen.put(Element.FIELD_BACKGROUND, Color.Transparent)
        darkGreen.put(Element.FIELD_BORDER, Color(0xFFb3f5c7))
        darkGreen.put(Element.BUTTON_COLOR, Color.Transparent)
        darkGreen.put(Element.BUTTON_BORDER, Color(0xFFb3f5c7))
        darkGreen.put(Element.BUTTON_TEXT, Color.White)
        darkGreen.put(Element.BUTTON_BACKGROUND, Color.Transparent)
        darkGreen.put(Element.CLICKABLE, Color.Yellow)
        darkGreen.put(Element.ERROR_TEXT, Color(0xFFe897e7))
        darkGreen.put(Element.OTHER_TEXT, Color(0xFF2677e0))

        darkBrown.put(Element.BANNER, Color(0xFF4d2b02))
        darkBrown.put(Element.TEXT, Color(0xFFf0cd90))
        darkBrown.put(Element.BACKGROUND, Color(0xFF4d2b02))
        darkBrown.put(Element.FIELD_BACKGROUND, Color.Transparent)
        darkBrown.put(Element.FIELD_BORDER, Color(0xFFf0cd90))
        darkBrown.put(Element.BUTTON_COLOR, Color.Transparent)
        darkBrown.put(Element.BUTTON_BORDER, Color(0xFFf0cd90))
        darkBrown.put(Element.BUTTON_TEXT, Color.White)
        darkBrown.put(Element.BUTTON_BACKGROUND, Color.Transparent)
        darkBrown.put(Element.CLICKABLE, Color.Yellow)
        darkBrown.put(Element.ERROR_TEXT, Color(0xFFe897e7))
        darkBrown.put(Element.OTHER_TEXT, Color(0xFF2677e0))

        darkPurple.put(Element.BANNER, Color(0xFF3a056b))
        darkPurple.put(Element.TEXT, Color(0xFFbc9bfa))
        darkPurple.put(Element.BACKGROUND, Color(0xFF3a056b))
        darkPurple.put(Element.FIELD_BACKGROUND, Color.Transparent)
        darkPurple.put(Element.FIELD_BORDER, Color(0xFFbc9bfa))
        darkPurple.put(Element.BUTTON_COLOR, Color.Transparent)
        darkPurple.put(Element.BUTTON_BORDER, Color(0xFFbc9bfa))
        darkPurple.put(Element.BUTTON_TEXT, Color.White)
        darkPurple.put(Element.BUTTON_BACKGROUND, Color.Transparent)
        darkPurple.put(Element.CLICKABLE, Color.Yellow)
        darkPurple.put(Element.ERROR_TEXT, Color(0xFFe897e7))
        darkPurple.put(Element.OTHER_TEXT, Color(0xFF2677e0))

        light.put(Element.BACKGROUND, Color(0xFFabe2ed))
        light.put(Element.USER_MESSAGE, Color(0xFF2677e0))
        light.put(Element.BOT_MESSAGE, Color(0xFF04470e))
        light.put(Element.FIELD_BORDER, Color(0xFF2677e0))
        //light.put(Element.FIELD_BACKGROUND, Color.White)
        light.put(Element.SEND_ICON, Color(0xFF088a36))
        light.put(Element.BOTTOM_ICON, Color.White)
        light.put(Element.BOTTOM_BACKGROUND, Color(0xFF2677e0))
        light.put(Element.BOTTOM_UNSELECTED, Color(0xFF0b29bd))
        //light.put(Element.ERROR_TEXT, Color.Red)

        dark.put(Element.BACKGROUND, Color(0xFF032e73))
        dark.put(Element.USER_MESSAGE, Color(0xFFe5f01d))
        dark.put(Element.BOT_MESSAGE, Color(0xFFe897e7))
        dark.put(Element.FIELD_BORDER, Color(0xFF94d0f2))
        dark.put(Element.SEND_ICON, Color(0xFFb3f5c7))
        dark.put(Element.BOTTOM_ICON, Color.Yellow)
        dark.put(Element.BOTTOM_BACKGROUND, Color(0xFF032e73))
        dark.put(Element.BOTTOM_UNSELECTED, Color(0xFFb3f5c7))
        //dark.put(Element.ERROR_TEXT, Color(0xFFe897e7))

        colorScheme.put(ColorMode.LIGHT_BLUE, lightBlue)
        colorScheme.put(ColorMode.LIGHT_GREEN, lightGreen)
        colorScheme.put(ColorMode.LIGHT_BROWN, lightBrown)
        colorScheme.put(ColorMode.LIGHT_PURPLE, lightPurple)
        colorScheme.put(ColorMode.DARK_BLUE, darkBlue)
        colorScheme.put(ColorMode.DARK_GREEN, darkGreen)
        colorScheme.put(ColorMode.DARK_BROWN, darkBrown)
        colorScheme.put(ColorMode.DARK_PURPLE, darkPurple)
        colorScheme.put(ColorMode.LIGHT, light)
        colorScheme.put(ColorMode.DARK, dark)
    }

    fun getColor(mode: ColorMode, element: Element) : Color {
        return colorScheme[mode]!![element]!!
    }

    val lightBlueBackground = Color(0xFFabe2ed)
    val blueBackground = Color(0xFF2677e0)
    val lightPurpleBackground = Color(0xFFbc9bfa)
    val blueText = Color(0xFF0b29bd)
    val titleColor = Color(0xFF041a87)
    val errorColor = Color.Red
    val dialogBlueBackground = Color(0xFF59b3f0)

    val messageColorUser = Color(0xFF088a36)
}



