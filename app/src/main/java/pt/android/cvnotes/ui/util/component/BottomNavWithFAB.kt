package pt.android.cvnotes.ui.util.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.android.cvnotes.theme.MyTheme
import pt.android.cvnotes.ui.util.Screen


@Preview(showBackground = true)
@Composable
fun BottomBarWithFab(
    bottomNavItems: List<Screen> = Screen.Defaults,
    bottomNavSelected: Int = 0,
    pageListener: (Int) -> Unit = {},
    fabListener: () -> Unit = {},
    fabIcon: ImageVector = Icons.Filled.Edit
) {
    MyTheme {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier
                        .size(75.dp)
                        .offset(x = 0.dp, y = 80.dp)
                        .border(
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimary),
                            shape = RoundedCornerShape(15.dp)
                        ),
                    shape = RoundedCornerShape(15.dp),
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = { fabListener.invoke() }
                ) {
                    Icon(imageVector = fabIcon, contentDescription = "Add icon")
                }
            },
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier.height(80.dp),
                    containerColor = MaterialTheme.colorScheme.primary
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
        modifier = Modifier.size(65.dp),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            modifier = Modifier.fillMaxSize(),
            onClick = { pageListener.invoke(index) }
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = if (index == bottomNavSelected) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.secondary
                }
            )
        }
    }
}