package com.viictrp.financeapp.ui.components.custom

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import coil.compose.rememberAsyncImagePainter
import com.viictrp.financeapp.R
import com.viictrp.financeapp.application.dto.UserDTO
import com.viictrp.financeapp.ui.components.icon.CustomIcons
import com.viictrp.financeapp.ui.theme.FinanceAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(user: UserDTO?) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = {
            Text(
                user?.fullName ?: "",
                Modifier.padding(start = 8.dp),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        navigationIcon = {
            Image(
                painter = user?.let {
                    rememberAsyncImagePainter(it.pictureUrl)
                } ?: painterResource(R.drawable.ic_launcher_background),
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
                    CustomIcons.Filled.Settings,
                    contentDescription = "Settings",
                    modifier = Modifier.size(32.dp)
                )
            }
            IconButton(
                modifier = Modifier.padding(end = 16.dp),
                onClick = { /* Sync or refresh */ }) {
                Icon(
                    CustomIcons.Filled.User,
                    contentDescription = "Sync",
                    modifier = Modifier
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
        Header(
            UserDTO(
                fullName = "Victor",
                email = "a@a.com",
                pictureUrl = "",
                accessToken = ""
            )
        )
    }
}