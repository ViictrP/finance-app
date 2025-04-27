import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.NumberFormat
import java.util.*

class CurrencyVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val input = text.text.filter { it.isDigit() }
        val parsed = input.toLongOrNull() ?: 0L

        val formatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        val formatted = formatter.format(parsed / 100.0)

        return TransformedText(AnnotatedString(formatted), OffsetMapping.Identity)
    }
}