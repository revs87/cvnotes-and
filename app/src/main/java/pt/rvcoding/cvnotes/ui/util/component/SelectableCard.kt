package pt.rvcoding.cvnotes.ui.util.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RippleConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pt.rvcoding.cvnotes.theme.BackgroundCardColor
import pt.rvcoding.cvnotes.theme.Blue500_Background3
import pt.rvcoding.cvnotes.theme.Blue700
import pt.rvcoding.cvnotes.theme.MyTheme
import pt.rvcoding.cvnotes.theme.SpMedium
import pt.rvcoding.cvnotes.theme.TextColor
import pt.rvcoding.cvnotes.ui.util.component.cvn.CVNText


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectableCard(
    modifier: Modifier,
    id: Long,
    isSelected: Boolean,
    hasSelected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    cardContentColor: Color = BackgroundCardColor,
    cardContentPadding: Dp = 0.dp,
    cardDefaultMinSize: Dp = 0.dp,
    content: @Composable RowScope.() -> Unit,
) {
    val targetContainerColor by animateColorAsState(
        targetValue = if (isSelected) Blue500_Background3 else Blue500_Background3,
        label = "containerColorAnimation"
    )
    val targetContentColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
        label = "contentColorAnimation"
    )
    val targetBorderColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
        label = "borderColorAnimation"
    )
    val clickRippleConfig = RippleConfiguration(
        color = Blue700
    )
    CompositionLocalProvider(LocalRippleConfiguration provides clickRippleConfig) {
        OutlinedCard(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.Transparent),
            shape = MaterialTheme.shapes.medium,
            border = BorderStroke(2.dp, targetBorderColor),
            colors = CardDefaults.cardColors(
                containerColor = targetContainerColor,
                contentColor = targetContentColor
            )
        ) {
            Box(
                modifier = Modifier
                    .background(cardContentColor)
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = cardDefaultMinSize)
                    .combinedClickable(
                        onClick = {
                            if (hasSelected) {
                                onLongClick.invoke()
                            } else {
                                onClick.invoke()
                            }
                        },
                        onLongClick = { onLongClick.invoke() },
                        role = Role.Checkbox
                    )
            ) {
                Row(
                    modifier = Modifier.padding(cardContentPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    content.invoke(this)

                    Spacer(Modifier.width(16.dp))

                    if (isSelected) {
                        Icon(
                            modifier = Modifier.padding(4.dp),
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Selected_$id",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectableCardPreview() {
    MyTheme {
        SelectableCard(
            modifier = Modifier,
            id = 0L,
            isSelected = true,
            hasSelected = true,
            onClick = {},
            onLongClick = {},
            content = {
                CVNText(
                    modifier = Modifier.weight(1f),
                    text = "Hello my friends!",
                    lineHeight = SpMedium,
                    fontSize = SpMedium,
                    textAlign = TextAlign.Start,
                    color = TextColor
                )
            },
        )
    }
}