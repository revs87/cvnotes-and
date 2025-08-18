package pt.rvcoding.cvnotes.ui.util.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.PictureAsPdf
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.rvcoding.cvnotes.theme.BackgroundColor
import pt.rvcoding.cvnotes.theme.Blue400
import pt.rvcoding.cvnotes.theme.Blue500
import pt.rvcoding.cvnotes.theme.Blue500_Background3
import pt.rvcoding.cvnotes.theme.MyTheme
import pt.rvcoding.cvnotes.theme.SpSmall
import pt.rvcoding.cvnotes.ui.isLandscape
import pt.rvcoding.cvnotes.ui.util.Screen
import pt.rvcoding.cvnotes.ui.util.component.cvn.CVNText


@Preview(showBackground = true)
@Composable
fun BottomBarWithFab(
    bottomNavItems: List<Screen> = Screen.Defaults,
    bottomNavSelected: Int = 0,
    pageListener: (Int) -> Unit = {},
    aiGenerateListener: () -> Unit = {},
    smallFabClickListener: () -> Unit = {},
    fabClickListener: () -> Unit = {},
    fabIcon: ImageVector = Icons.Filled.Edit,
    fabVisible: Boolean = true,
    smallFabVisible: Boolean = true,
    aiGenerateVisible: Boolean = true
) {
    MyTheme {
        Scaffold(
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(top = 0, left = 0),
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Bottom,
                ) {
                    // Show button only if no loading and no notes selected
                    var showAIButton by remember { mutableStateOf(true) }
                    LaunchedEffect(aiGenerateVisible && fabVisible) {
                        showAIButton = aiGenerateVisible && fabVisible
                    }
                    AIButton(
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .offset(x = if (isLandscape()) (-60).dp else 0.dp, y = 80.dp),
                        generateListener = { aiGenerateListener.invoke() },
                        visible = showAIButton
                    )

                    if (smallFabVisible) {
                        SmallFloatingActionButton(
                            modifier = Modifier
                                .size(45.dp)
                                .offset(x = if (isLandscape()) (-60).dp else 0.dp, y = 80.dp),
                            onClick = { smallFabClickListener.invoke() },
                            containerColor = Blue500,
                            shape = RoundedCornerShape(12.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.PictureAsPdf,
                                contentDescription = "Location FAB",
                                tint = Blue500_Background3,
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    if (fabVisible) {
                        FloatingActionButton(
                            modifier = Modifier
                                .size(75.dp)
                                .offset(x = if (isLandscape()) (-60).dp else 0.dp, y = 80.dp)
                                .border(
                                    border = BorderStroke(1.dp, Blue500),
                                    shape = RoundedCornerShape(15.dp)
                                ),
                            shape = RoundedCornerShape(15.dp),
                            contentColor = Blue500_Background3,
                            containerColor = Blue500,
                            onClick = { fabClickListener.invoke() }
                        ) { Icon(imageVector = fabIcon, contentDescription = fabIcon.name) }
                    }
                }
            },
            bottomBar = {
                BottomAppBar(
                    containerColor = BackgroundColor
                ) {
                    LazyRow(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        itemsIndexed(bottomNavItems) { index, item ->
                            MenuButton(
                                pageListener,
                                index,
                                item,
                                bottomNavSelected
                            )
                        }
                    }
                }
            },
        ) { contentPadding ->
            Box(modifier = Modifier.padding(contentPadding)) {
                bottomNavItems[bottomNavSelected].content()
            }
        }
    }
}

@Composable
private fun MenuButton(
    pageListener: (Int) -> Unit = {},
    index: Int = 0,
    item: Screen = Screen.Default,
    bottomNavSelected: Int = 0
) {
    Box(
        modifier = Modifier.height(65.dp),
        contentAlignment = Alignment.Center
    ) {
        TextButton(
            modifier = Modifier.fillMaxSize(),
            onClick = { pageListener.invoke(index) }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = if (index == bottomNavSelected) { Blue500 } else { Blue400 }
                )
                CVNText(
                    modifier = Modifier.padding(top = 2.dp),
                    text = item.title.uppercase(),
                    fontSize = SpSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = if (index == bottomNavSelected) { Blue500 } else { Blue400 }
                )
            }
        }
    }
}