package com.bitpunchlab.android.jesschatlex2.base

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import com.bitpunchlab.android.jesschatlex2.R
import com.bitpunchlab.android.jesschatlex2.helpers.WhoSaid
import com.bitpunchlab.android.jesschatlex2.ui.theme.JessChatLex

@Composable
fun UserInputTextField(title: String, content: String, hide: Boolean,
                       modifier: Modifier,
                       textColor: Color = JessChatLex.blueText,
                       textBorder: Color = JessChatLex.blueBackground,
                       textSize: TextUnit = dimensionResource(id = R.dimen.general_text_size).value.sp,
                       fieldBackground: Color = Color.White,
                       fieldBorder: Color = JessChatLex.blueBackground,
                        call: (String) -> Unit)  {

    val keyboardType = if (hide) { KeyboardType.Password } else { KeyboardType.Text }

    // this is the color of the cursor handle
    val customTextSelectionColors = TextSelectionColors(
        handleColor = textColor,
        backgroundColor = textColor,
    )

    CompositionLocalProvider(
        LocalTextSelectionColors provides customTextSelectionColors,
    ) {
        OutlinedTextField(
            label = {
                Text(
                    text = title,
                    color = textColor,
                    fontSize = textSize
                )
            },
            value = content,
            onValueChange = { newText ->
                call.invoke(newText)
            },

            modifier = Modifier
                //.fillMaxWidth()
                .then(modifier),

            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            textStyle = LocalTextStyle.current.copy(color = textColor, fontSize = textSize),

            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = fieldBorder,
                unfocusedBorderColor = fieldBorder,
                focusedLabelColor = textBorder,
                unfocusedLabelColor = textBorder,
                cursorColor = textColor,
                backgroundColor = fieldBackground,
                textColor = textColor,
                ),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.normal_round_corner)),
        )
    }
}

@Composable
fun TitleText(title: String, modifier: Modifier) {
    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
            //.padding(top = paddingTop.dp, bottom = paddingBottom.dp),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h1,
        fontSize = dimensionResource(id = R.dimen.title_text_size).value.sp,
        color = Color.White
    )
}

@Composable
fun GeneralText(textString: String, textColor: Color = MaterialTheme.colors.primary,
                textAlign: TextAlign = TextAlign.Start,
                size: TextUnit = 18.sp, modifier: Modifier,
                onClick: (() -> Unit)? = null) {
    Text(
        text = textString,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                //start = dimensionResource(id = R.dimen.textfield_left_padding),
                //end = dimensionResource(id = R.dimen.textfield_right_padding)
            )
            .clickable(enabled = onClick != null) {
                onClick?.invoke()
            }
            .then(modifier),
        color = textColor,
        fontSize = size,
        style = MaterialTheme.typography.body1,
        textAlign = textAlign
    )
}

@Composable
fun ErrorText(error: String, color: Color = Color.Red,
              size: TextUnit = dimensionResource(id = R.dimen.general_text_size).value.sp,
              modifier: Modifier) {
    Text(
        text = error,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 3.dp
            )
            .then(modifier),
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.body2,
        color = color,
        fontSize = size
    )
}

@Composable
fun AppButton(title: String, onClick: () -> Unit, shouldEnable: Boolean,
              buttonColor: Color = JessChatLex.blueBackground,
              buttonBackground: Color = JessChatLex.lightBlueBackground,
              buttonBorder: Color = JessChatLex.blueBackground,
              modifier: Modifier) {

    OutlinedButton(
        onClick = { onClick.invoke() },

        colors = ButtonDefaults.buttonColors(
            backgroundColor = buttonColor,
            disabledContentColor = Color.Gray,
            disabledBackgroundColor = Color.Gray,

        ),
        modifier = Modifier
            //.fillMaxWidth()
            .padding()
            .background(buttonBackground)
            .border(BorderStroke(2.dp, buttonBorder), RoundedCornerShape(15.dp))
            .then(modifier),

        shape = RoundedCornerShape(dimensionResource(id = R.dimen.normal_round_corner)),
        enabled = shouldEnable)
    {
        Text(
            text = title,
            fontSize = dimensionResource(id = R.dimen.bigger_text_size).value.sp,
            color = Color.White
        )
    }
}

@Composable
fun HeaderImage(resource: Int, description: String, paddingTop: Int, paddingBottom: Int) {
    Image(
        painter = painterResource(id = resource),
        modifier = Modifier
            .fillMaxWidth(0.4f)
            .padding(top = paddingTop.dp, bottom = paddingBottom.dp),
        contentDescription = description,

        )
}

@Composable
fun CustomDialog(title: String, message: String, backgroundColor: Color = JessChatLex.dialogBlueBackground,
                 buttonColor: Color = JessChatLex.blueText, buttonBorder: Color = JessChatLex.blueText,
                 textColor: Color = JessChatLex.blueText, fieldBackground: Color = Color.White,
                 fieldOne: String? = null, fieldTwo: String? = null,
                 onDismiss: () -> Unit,
                 okOnClick: ((String?, String?) -> Unit)? = null, cancelOnClick: ((String?, String?) -> Unit)? = null,
                 errorString: String? = null
) {
    Log.i("custom dialog", "got error string $errorString")

    var fieldOneValue by remember {
        mutableStateOf("")
    }

    var fieldTwoValue by remember {
        mutableStateOf("")
    }

    Dialog(onDismissRequest = { onDismiss.invoke() }) {
        Surface(
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.more_round_corner)),
            color = backgroundColor
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = dimensionResource(id = R.dimen.bit_more_space),
                        bottom = dimensionResource(id = R.dimen.bit_more_space),
                        start = dimensionResource(id = R.dimen.bit_more_space),
                        end = dimensionResource(id = R.dimen.bit_more_space)
                    )
            ) {

                Text(
                    text = title,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h3,
                    color = textColor,
                    fontSize = dimensionResource(id = R.dimen.normal_text_size).value.sp
                )

                Text(
                    text = message,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = dimensionResource(id = R.dimen.bit_more_space),
                            start = dimensionResource(id = R.dimen.bit_more_space),
                            end = dimensionResource(id = R.dimen.bit_more_space)
                        ),
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.body1,
                    color = textColor,
                    fontSize = dimensionResource(id = R.dimen.general_text_size).value.sp
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (fieldOne != null) {
                        UserInputTextField(
                            title = fieldOne,
                            content = fieldOneValue,
                            hide = false,
                            fieldBorder = buttonBorder,
                            fieldBackground = fieldBackground,
                            textColor = textColor,
                            modifier = Modifier.padding(
                                top = dimensionResource(id = R.dimen.bit_more_space),
                                start = dimensionResource(id = R.dimen.bit_more_space),
                                end = dimensionResource(id = R.dimen.bit_more_space)),
                            call = { fieldOneValue = it })
                    }

                    if (fieldTwo != null) {
                        UserInputTextField(
                            title = fieldTwo,
                            content = fieldTwoValue,
                            hide = false,
                            fieldBorder = buttonBorder,
                            fieldBackground = fieldBackground,
                            textColor = textColor,
                            modifier = Modifier.padding(
                                top = dimensionResource(id = R.dimen.bit_more_space),
                                start = dimensionResource(id = R.dimen.bit_more_space),
                                end = dimensionResource(id = R.dimen.bit_more_space)),
                            call = { fieldTwoValue = it })
                    }

                    if (okOnClick != null && cancelOnClick != null) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = dimensionResource(id = R.dimen.bit_more_space),
                                    start = dimensionResource(id = R.dimen.bit_more_space),
                                    end = dimensionResource(id = R.dimen.bit_more_space)
                                ),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            DialogButton(
                                title = stringResource(R.string.ok),
                                stateOne = fieldOneValue,
                                stateTwo = fieldTwoValue,
                                color = buttonColor,
                                border = buttonBorder,
                                onClick = okOnClick,
                                modifier = Modifier,
                            )
                            DialogButton(
                                title = stringResource(R.string.cancel),
                                color = buttonColor,
                                border = buttonBorder,
                                onClick = cancelOnClick,
                                modifier = Modifier
                            )
                            if (errorString != null) {
                                ErrorText(
                                    error = errorString,
                                    modifier = Modifier.padding(top = 5.dp)
                                )
                            }
                        }
                    } else if (okOnClick != null){
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = dimensionResource(id = R.dimen.bit_more_space),
                                    start = dimensionResource(id = R.dimen.bit_more_space),
                                    end = dimensionResource(id = R.dimen.bit_more_space)
                                ),
                                horizontalArrangement = Arrangement.Center
                        ) {
                            DialogButton(
                                title = stringResource(R.string.ok),
                                color = buttonColor,
                                border = buttonBorder,
                                onClick = okOnClick,
                                modifier = Modifier,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DialogButton(title: String, color: Color = JessChatLex.blueBackground,
                 border: Color = JessChatLex.blueBackground,
                 stateOne: String? = null, stateTwo: String? = null,
                 onClick: (String?, String?) -> Unit, modifier: Modifier) {

    OutlinedButton(
        onClick = { onClick.invoke(stateOne, stateTwo) },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = color,
        ),

        modifier = modifier
            .border(BorderStroke(2.dp, border), RoundedCornerShape(10.dp))
    ) {
        Text(
            text = title,
            fontSize = dimensionResource(id = R.dimen.general_text_size).value.sp,
            color = Color.White
        )
    }
}

@Composable
fun CustomCircularProgressBar() {
    CircularProgressIndicator(
        modifier = Modifier.size(80.dp),
        color = JessChatLex.blueBackground,
        strokeWidth = 10.dp)

}

@Composable
fun SendIcon(color: Color, onClick: () -> Unit) {
    IconButton(
        onClick = { onClick.invoke() }
    ) {
        Icon(
            painter = painterResource(id = R.mipmap.send),
            contentDescription = stringResource(R.string.send_message),
            Modifier.size(30.dp),
            tint = color
        )
    }
}
