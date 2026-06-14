/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components.product

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.siamdev.zappos.data.source.MasterEvent
import org.siamdev.zappos.ui.screens.product.goods.sampleEvents
import org.siamdev.zappos.utils.formatPrice
import kotlin.math.sin

private enum class SalesPeriod(val label: String, val days: Int) {
    D7("7d", 7),
    D30("30d", 30),
    D90("90d", 90),
}

private fun fakeSparkline(seed: Int, count: Int): List<Float> {
    val base = (seed * 7 % 40 + 30).toFloat()
    return List(count) { i ->
        val wave = sin((i + seed) * 0.8).toFloat() * 18f
        val trend = i * (2.5f + seed % 3)
        (base + wave + trend).coerceAtLeast(10f)
    }
}

/** Card showing revenue total and a sparkline chart for the selected period (7 d / 30 d / 90 d). */
@Composable
fun SalesChartCard(event: MasterEvent) {
    var period by remember { mutableStateOf(SalesPeriod.D7) }
    val points = remember(event.id, period) { fakeSparkline(event.id.hashCode(), period.days) }
    val primaryColor = MaterialTheme.colorScheme.primary

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(0.dp),
        border = CardDefaults.outlinedCardBorder(),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column {
                    Text(
                        "SALES · LAST ${period.days} DAYS",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "฿${event.revenue7d.formatPrice()}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Spacer(Modifier.width(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.AutoMirrored.Filled.TrendingUp,
                                contentDescription = null,
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(14.dp),
                            )
                            Text(
                                " 9%",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF4CAF50),
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    SalesPeriod.entries.forEach { p ->
                        val active = p == period
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (active) primaryColor else Color.Transparent)
                                .clickable { period = p }
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                        ) {
                            Text(
                                p.label,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (active) FontWeight.Bold else FontWeight.Normal,
                                color = if (active) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Canvas(modifier = Modifier.fillMaxWidth().height(80.dp)) {
                drawSparkline(points, primaryColor)
            }
        }
    }
}

/** Draws a smooth cubic-bezier sparkline with a gradient fill onto the [DrawScope]. */
private fun DrawScope.drawSparkline(points: List<Float>, color: Color) {
    if (points.size < 2) return
    val minVal = points.min()
    val maxVal = points.max()
    val range = (maxVal - minVal).coerceAtLeast(1f)
    val w = size.width
    val h = size.height
    val step = w / (points.size - 1)

    fun px(i: Int) = i * step
    fun py(v: Float) = h - ((v - minVal) / range) * h * 0.85f - h * 0.05f

    val line = Path()
    val fill = Path()

    line.moveTo(px(0), py(points[0]))
    fill.moveTo(px(0), h)
    fill.lineTo(px(0), py(points[0]))

    for (i in 1 until points.size) {
        val cx = (px(i - 1) + px(i)) / 2f
        line.cubicTo(cx, py(points[i - 1]), cx, py(points[i]), px(i), py(points[i]))
        fill.cubicTo(cx, py(points[i - 1]), cx, py(points[i]), px(i), py(points[i]))
    }
    fill.lineTo(px(points.size - 1), h)
    fill.close()

    drawPath(
        fill,
        brush = Brush.verticalGradient(
            colors = listOf(color.copy(alpha = 0.28f), color.copy(alpha = 0.03f)),
            startY = 0f,
            endY = h,
        ),
    )
    drawPath(line, color = color, style = Stroke(width = 2.5f, cap = StrokeCap.Round))
}

@Preview(showBackground = true)
@Composable
private fun SalesChartCardPreview() {
    MaterialTheme {
        Box(Modifier.padding(16.dp)) {
            SalesChartCard(event = sampleEvents().first())
        }
    }
}
