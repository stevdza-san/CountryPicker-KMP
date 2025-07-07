package com.stevdza_san.library.component

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.stevdza_san.library.domain.Country
import com.stevdza_san.library.domain.CountryDisplayOption
import com.stevdza_san.library.domain.formatedName
import org.jetbrains.compose.resources.painterResource

@Composable
fun CountryPickerField(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selectedCountry: Country,
    displayOption: CountryDisplayOption = CountryDisplayOption.DIAL_CODE,
    horizontalPadding: Dp = 16.dp,
    verticalPadding: Dp = 16.dp,
    spacing: Dp = 12.dp,
    showIcon: Boolean = true,
    iconSize: Dp = 20.dp,
    shape: Shape = RoundedCornerShape(size = 6.dp),
    borderWidth: Dp = 1.dp,
    borderColor: Color = if (enabled) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.outline.copy(
        0.1f
    ),
    containerColor: Color = if (enabled) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface.copy(
        0.1f
    ),
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit,
) {
    val saturation = remember { Animatable(if (enabled) 1f else 0f) }

    LaunchedEffect(enabled) {
        saturation.animateTo(if (enabled) 1f else 0f)
    }

    val colorMatrix = remember(saturation.value) {
        ColorMatrix().apply {
            setToSaturation(saturation.value)
        }
    }

    Row(
        modifier = modifier
            .clip(shape)
            .background(containerColor)
            .border(
                width = borderWidth,
                color = borderColor,
                shape = shape
            )
            .clickable(
                enabled = enabled,
                onClick = onClick
            )
            .padding(
                vertical = verticalPadding,
                horizontal = horizontalPadding
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showIcon) {
            Image(
                modifier = Modifier.size(size = iconSize),
                painter = painterResource(selectedCountry.flag),
                contentDescription = "Country Flag",
                colorFilter = ColorFilter.colorMatrix(colorMatrix)
            )
            Spacer(modifier = Modifier.width(width = spacing))
        }
        Text(
            modifier = Modifier.alpha(if (enabled) 1f else 0.5f),
            text = when (displayOption) {
                CountryDisplayOption.NAME -> {
                    selectedCountry.formatedName()
                }

                CountryDisplayOption.CURRENCY -> {
                    selectedCountry.currency
                }

                CountryDisplayOption.DIAL_CODE -> {
                    "+${selectedCountry.dialCode}"
                }

                CountryDisplayOption.ISO_CODE -> {
                    "+${selectedCountry.isoCode}"
                }

                CountryDisplayOption.DIAL_CODE_AND_NAME -> {
                    "(+${selectedCountry.dialCode}) ${selectedCountry.formatedName()}"
                }

                CountryDisplayOption.NAME_AND_CURRENCY -> {
                    "${selectedCountry.formatedName()} (${selectedCountry.currency})"
                }

                CountryDisplayOption.NAME_AND_ISO_CODE -> {
                    "${selectedCountry.formatedName()} (${selectedCountry.isoCode})"
                }
            },
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = contentColor
        )
    }
}