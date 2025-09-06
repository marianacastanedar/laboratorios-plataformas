package com.example.labpratorio7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.labpratorio7.ui.theme.Labpratorio7Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Labpratorio7Theme {
                NotificationsScreen(
                    notifications = generateFakeNotifications(),
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    notifications: List<Notification>,
    modifier: Modifier = Modifier
) {
    var selected by rememberSaveable { mutableStateOf<NotificationType?>(null) }
    val list = if (selected == null) notifications else notifications.filter { it.type == selected }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Notificaciones") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Tipos de notificaciones",
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterChip(
                    selected = selected == NotificationType.GENERAL,
                    onClick = {
                        selected = if (selected == NotificationType.GENERAL) null else NotificationType.GENERAL
                    },
                    label = { Text("Informativas") },
                    leadingIcon = { Icon(Icons.Filled.Notifications, contentDescription = null) },
                    enabled = true
                )
                FilterChip(
                    selected = selected == NotificationType.NEW_MEETING,
                    onClick = {
                        selected = if (selected == NotificationType.NEW_MEETING) null else NotificationType.NEW_MEETING
                    },
                    label = { Text("Capacitaciones") },
                    leadingIcon = { Icon(Icons.Filled.Event, contentDescription = null) },
                    enabled = true
                )
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(list, key = { it.id }) { n ->
                    NotificationCardLikeMock(n)
                }
            }
        }
    }
}

@Composable
private fun NotificationCardLikeMock(n: Notification) {
    val badgeBg =
        if (n.type == NotificationType.GENERAL) MaterialTheme.colorScheme.tertiaryContainer
        else MaterialTheme.colorScheme.secondaryContainer
    val badgeFg =
        if (n.type == NotificationType.GENERAL) MaterialTheme.colorScheme.onTertiaryContainer
        else MaterialTheme.colorScheme.onSecondaryContainer
    val badgeIcon =
        if (n.type == NotificationType.GENERAL) Icons.Filled.Notifications else Icons.Filled.Event

    OutlinedCard(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                color = badgeBg,
                shape = CircleShape,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        badgeIcon,
                        contentDescription = null,
                        tint = badgeFg
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        n.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        n.sendAt,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        softWrap = false
                    )
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    n.body,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewNotifications() {
    Labpratorio7Theme {
        NotificationsScreen(
            notifications = generateFakeNotifications(),
            modifier = Modifier.fillMaxSize()
        )
    }
}
