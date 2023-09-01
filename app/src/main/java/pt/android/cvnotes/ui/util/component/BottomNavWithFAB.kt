package pt.android.cvnotes.ui.util.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.android.cvnotes.theme.BackgroundColor
import pt.android.cvnotes.theme.Blue400
import pt.android.cvnotes.theme.Blue500
import pt.android.cvnotes.theme.Blue500_Background3
import pt.android.cvnotes.theme.MyTheme
import pt.android.cvnotes.ui.util.Screen


@Preview(showBackground = true)
@Composable
fun BottomBarWithFab(
    bottomNavItems: List<Screen> = Screen.Defaults,
    bottomNavSelected: Int = 0,
    pageListener: (Int) -> Unit = {},
    fabClickListener: () -> Unit = {},
    fabIcon: ImageVector = Icons.Filled.Edit,
    fabVisible: Boolean = true
) {
    MyTheme {
        Scaffold(
            floatingActionButton = {
                if (fabVisible) {
                    FloatingActionButton(
                        modifier = Modifier
                            .size(75.dp)
                            .offset(x = 0.dp, y = 80.dp)
                            .border(
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimary),
                                shape = RoundedCornerShape(15.dp)
                            ),
                        shape = RoundedCornerShape(15.dp),
                        contentColor = Blue500_Background3,
                        containerColor = Blue500,
                        onClick = { fabClickListener.invoke() }
                    ) { Icon(imageVector = fabIcon, contentDescription = fabIcon.name) }
                }
            },
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier.height(80.dp),
                    containerColor = BackgroundColor
                ) {
                    LazyRow(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        itemsIndexed(bottomNavItems) { index, item ->
                            MenuButton(
                                pageListener,
                                index,
                                item,
                                bottomNavSelected)
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
                Text(
                    modifier = Modifier.padding(top = 2.dp),
                    text = item.title.uppercase(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (index == bottomNavSelected) { Blue500 } else { Blue400 }
                )
            }
        }
    }
}