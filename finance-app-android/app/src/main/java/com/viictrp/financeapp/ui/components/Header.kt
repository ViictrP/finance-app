package com.viictrp.financeapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viictrp.financeapp.R
import com.viictrp.financeapp.ui.theme.FinanceAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(name: String) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = {
            Text(
                name,
                Modifier.padding(start = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        navigationIcon = {
            Image(
                painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = "Profile",
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(40.dp)
                    .clip(CircleShape)
            )
        },
        actions = {
            IconButton(onClick = { /* Open settings */ }) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = "Settings",
                    modifier = Modifier.size(32.dp)
                )
            }
            IconButton(onClick = { /* Sync or refresh */ }) {
                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = "Sync",
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(32.dp)
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun HeaderPreview() {
    FinanceAppTheme {
        Header("Victor Prado")
    }
}