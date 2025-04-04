package com.koineos.app.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.koineos.app.R
import com.koineos.app.ui.components.core.RegularButton
import com.koineos.app.ui.theme.Colors
import com.koineos.app.ui.theme.Dimensions
import com.koineos.app.ui.theme.KoineosTheme
import com.koineos.app.ui.theme.MainFont
import com.koineos.app.ui.theme.Typography

@Composable
fun StartScreen(
    onStartLearning: () -> Unit,
    onLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Colors.PrimaryGradient
            ),
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_start),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.BottomCenter)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.White
                            )
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(Dimensions.paddingXLarge),
                horizontalAlignment = Alignment.Start
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_wordmark),
                    contentDescription = "LexiKoine",
                    modifier = Modifier.height(20.dp)
                )

                Spacer(modifier = Modifier.height(Dimensions.spacingXLarge))

                Text(
                    text = "Learn Koine Greek. Understand the Scriptures.",
                    style = Typography.headlineLarge.copy(
                        fontWeight = FontWeight.Black,
                        fontFamily = MainFont
                    ),
                    color = Colors.OnSurface
                )

                Spacer(modifier = Modifier.height(Dimensions.spacingXXLarge))

                RegularButton(
                    onClick = onStartLearning,
                    text = "Start learning now",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(Dimensions.spacingXLarge))

                Text(
                    text = buildAnnotatedString {
                        append("Already a LexiKoine user? ")
                        withStyle(
                            style = SpanStyle(
                                color = Colors.Primary,
                                fontWeight = FontWeight.Bold,
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            append("Log in")
                        }
                    },
                    color = Colors.TextPrimary,
                    style = Typography.bodyMedium,
                    modifier = Modifier.clickable {
                        onLogin()
                    }
                )

                Spacer(modifier = Modifier.height(Dimensions.spacingXXXLarge))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StartScreenPreview() {
    KoineosTheme {
        StartScreen(
            onStartLearning = {},
            onLogin = {}
        )
    }
}