package com.stevdza_san.library.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.stevdza_san.library.domain.Country
import com.stevdza_san.library.domain.CountryDisplayOption
import com.stevdza_san.library.domain.filterByQuery
import com.stevdza_san.library.domain.formatedName
import countrypicker_kmp.library.generated.resources.Res
import countrypicker_kmp.library.generated.resources.checkmark
import org.jetbrains.compose.resources.painterResource

@Composable
fun CountryPickerDialog(
    title: String = "Pick a Country",
    searchPlaceholder: String = "Search",
    searchPlaceholderShape: Shape = RoundedCornerShape(size = 99.dp),
    dismissButton: String = "Cancel",
    confirmButton: String = "Confirm",
    errorText: String = "Country not found.",
    displayOption: CountryDisplayOption = CountryDisplayOption.DIAL_CODE_AND_NAME,
    showIcon: Boolean = true,
    selectedCountry: Country,
    onConfirmClick: (Country) -> Unit,
    onDismiss: () -> Unit,
    selectorSize: Dp = 18.dp,
    selectorIconSize: Dp = 12.dp,
    primaryColor: Color = MaterialTheme.colorScheme.primary,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    selectorActiveColor: Color = MaterialTheme.colorScheme.primary,
    selectorInactiveColor: Color = MaterialTheme.colorScheme.outline.copy(0.1f),
    selectorIconColor: Color = MaterialTheme.colorScheme.onPrimary,
    searchBarColors: TextFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = contentColor.copy(0.05f),
        unfocusedContainerColor = contentColor.copy(0.05f),
        disabledContainerColor = contentColor.copy(0.05f),
        errorContainerColor = contentColor.copy(0.05f),
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        selectionColors = TextSelectionColors(
            handleColor = primaryColor,
            backgroundColor = primaryColor.copy(0.38f)
        )
    )
) {
    val allCountries = remember { Country.entries.toList() }
    val filteredCountries = remember {
        mutableStateListOf<Country>().apply {
            addAll(allCountries)
        }
    }
    var searchQuery by remember { mutableStateOf("") }
    var selectedCountryState by remember(selectedCountry) {
        mutableStateOf(selectedCountry)
    }

    AlertDialog(
        containerColor = containerColor,
        title = {
            Text(
                text = title,
                color = contentColor
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(searchPlaceholderShape),
                    value = searchQuery,
                    onValueChange = { query ->
                        searchQuery = query
                        if (query.isNotEmpty()) {
                            val filtered = allCountries.filterByQuery(query)
                            filteredCountries.clear()
                            filteredCountries.addAll(filtered)
                        } else {
                            filteredCountries.clear()
                            filteredCountries.addAll(Country.entries)
                        }
                    },
                    placeholder = {
                        Text(
                            text = searchPlaceholder,
                            color = contentColor.copy(alpha = 0.38f),
                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        )
                    },
                    singleLine = true,
                    textStyle = TextStyle(
                        color = contentColor,
                        fontSize = MaterialTheme.typography.bodySmall.fontSize
                    ),
                    colors = searchBarColors
                )

                Spacer(modifier = Modifier.height(20.dp))

                AnimatedContent(
                    targetState = filteredCountries
                ) { availableCountries ->
                    if (availableCountries.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(
                                items = availableCountries,
                                key = { it.ordinal }
                            ) { country ->
                                CountryPicker(
                                    country = country,
                                    showIcon = showIcon,
                                    displayOption = displayOption,
                                    isSelected = selectedCountryState == country,
                                    onSelect = { selectedCountryState = country },
                                    selectorSize = selectorSize,
                                    selectorIconSize = selectorIconSize,
                                    contentColor = contentColor,
                                    selectorActiveColor = selectorActiveColor,
                                    selectorInactiveColor = selectorInactiveColor,
                                    selectorIconColor = selectorIconColor
                                )
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .background(containerColor)
                                .height(250.dp)
                                .fillMaxSize()
                                .padding(horizontal = 24.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = errorText,
                                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                textAlign = TextAlign.Center,
                                color = contentColor
                            )
                        }
                    }
                }
            }
        },
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = dismissButton,
                    color = contentColor.copy(alpha = 0.5f)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirmClick(selectedCountryState) },
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = primaryColor
                )
            ) {
                Text(text = confirmButton)
            }
        }
    )
}

@Composable
private fun CountryPicker(
    country: Country,
    showIcon: Boolean,
    displayOption: CountryDisplayOption,
    isSelected: Boolean,
    onSelect: () -> Unit,
    selectorSize: Dp,
    selectorIconSize: Dp,
    contentColor: Color,
    selectorActiveColor: Color,
    selectorInactiveColor: Color,
    selectorIconColor: Color,
) {
    val saturation = remember { Animatable(if (isSelected) 1f else 0f) }

    LaunchedEffect(isSelected) {
        saturation.animateTo(if (isSelected) 1f else 0f)
    }

    val colorMatrix = remember(saturation.value) {
        ColorMatrix().apply {
            setToSaturation(saturation.value)
        }
    }

    val animatedAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.5f,
        animationSpec = tween(durationMillis = 300)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(size = 6.dp))
            .clickable { onSelect() }
            .padding(all = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (showIcon) {
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(country.flag),
                    contentDescription = "Country Flag",
                    colorFilter = ColorFilter.colorMatrix(colorMatrix)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                modifier = Modifier.alpha(animatedAlpha),
                text = when (displayOption) {
                    CountryDisplayOption.NAME -> {
                        country.formatedName()
                    }

                    CountryDisplayOption.CURRENCY -> {
                        country.currency
                    }

                    CountryDisplayOption.DIAL_CODE -> {
                        "+${country.dialCode}"
                    }

                    CountryDisplayOption.DIAL_CODE_AND_NAME -> {
                        "(+${country.dialCode}) ${country.formatedName()}"
                    }

                    CountryDisplayOption.NAME_AND_CURRENCY -> {
                        "${country.formatedName()} (${country.currency})"
                    }
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = contentColor
            )
        }
        Selector(
            isSelected = isSelected,
            activeColor = selectorActiveColor,
            inactiveColor = selectorInactiveColor,
            iconColor = selectorIconColor,
            size = selectorSize,
            iconSize = selectorIconSize
        )
    }
}

@Composable
private fun Selector(
    isSelected: Boolean = false,
    activeColor: Color,
    inactiveColor: Color,
    size: Dp,
    iconColor: Color,
    iconSize: Dp,
) {
    val animatedColor by animateColorAsState(
        targetValue = if (isSelected) activeColor else inactiveColor,
        animationSpec = tween(durationMillis = 300)
    )
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(animatedColor),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                modifier = Modifier.size(iconSize),
                painter = painterResource(Res.drawable.checkmark),
                contentDescription = "Checkmark icon",
                tint = iconColor
            )
        }
    }
}