import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.phonebook.R
import com.example.phonebook.ui.theme.PhoneBookThemeSettings
import com.example.phonebook.util.fromHex

@Composable
fun ContactColor(
    modifier: Modifier = Modifier,
    icon: Int,
    size: Dp,
    border: Dp
) {
    val isDarkTheme = PhoneBookThemeSettings.isDarkThemeEnabled

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(Color.Transparent)
            .border(
                BorderStroke(
                    border,
                    Color.Black
                ),
                CircleShape
            )
    ) {
        if (!isDarkTheme)
            Icon(
                modifier = Modifier.size(size),
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = Color.Black
            )
        else
            Icon(
                modifier = Modifier.size(size),
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = Color.White
            )

    }
}

@Preview
@Composable
fun ContactColorPreview() {
    ContactColor(
        icon = R.drawable.baseline_phone_android_24,
        size = 40.dp,
        border = 2.dp
    )
}