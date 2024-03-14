package com.jobik.gameoflife.screens.Game.actions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobik.gameoflife.R
import com.jobik.gameoflife.screens.Game.GameScreenViewModel

@Composable
fun ChangeGameFieldDimension(viewModel: GameScreenViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        var isRelated by rememberSaveable { mutableStateOf(true) }
        val rows = remember { mutableStateOf(viewModel.states.value.rows.toString()) }
        val cols = remember { mutableStateOf(viewModel.states.value.cols.toString()) }

        LaunchedEffect(rows.value, cols.value, isRelated) {
            if (rows.value.isBlank() || cols.value.isBlank()) return@LaunchedEffect
            viewModel.setRows(rows.value)
            if (isRelated) {
                cols.value = rows.value
            }
            viewModel.setColumns(cols.value)
        }

        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(
                text = stringResource(id = R.string.set_field_dimensions),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )
            BoxWithConstraints {
                if (maxWidth > 250.dp) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.weight(1f),
                            value = rows.value,
                            onValueChange = { rows.value = it },
                            label = {
                                Text(text = stringResource(id = R.string.rows))
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        OutlinedTextField(
                            modifier = Modifier.weight(1f),
                            value = cols.value,
                            onValueChange = { cols.value = it },
                            label = {
                                Text(text = stringResource(id = R.string.columns))
                            },
                            enabled = !isRelated,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        Switch(
                            checked = isRelated,
                            onCheckedChange = { isRelated = !isRelated },
                            thumbContent = if (isRelated) {
                                {
                                    Icon(
                                        imageVector = Icons.Outlined.Link,
                                        contentDescription = null,
                                        modifier = Modifier.size(SwitchDefaults.IconSize),
                                    )
                                }
                            } else {
                                null
                            }
                        )
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = rows.value,
                            onValueChange = { rows.value = it },
                            label = {
                                Text(text = stringResource(id = R.string.rows))
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                modifier = Modifier.weight(1f),
                                value = cols.value,
                                onValueChange = { cols.value = it },
                                label = {
                                    Text(text = stringResource(id = R.string.columns))
                                },
                                enabled = !isRelated,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                            Switch(
                                checked = isRelated,
                                onCheckedChange = { isRelated = !isRelated },
                                thumbContent = if (isRelated) {
                                    {
                                        Icon(
                                            imageVector = Icons.Outlined.Link,
                                            contentDescription = null,
                                            modifier = Modifier.size(SwitchDefaults.IconSize),
                                        )
                                    }
                                } else {
                                    null
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}