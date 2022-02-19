package clovis.ui.dimensions

import org.jetbrains.compose.web.css.pt

// Inspired by https://stackoverflow.com/a/41126784/5666171
fun DensityPixel.toCss() = ((72 / 160) * value).pt
