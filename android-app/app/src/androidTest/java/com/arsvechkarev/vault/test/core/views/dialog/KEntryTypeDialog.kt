package com.arsvechkarev.vault.test.core.views.dialog

import android.widget.LinearLayout
import com.arsvechkarev.vault.R
import io.github.kakaocup.kakao.common.views.KBaseView

class KEntryTypeDialog(
  private val id: Int
) : KBaseView<KEntryTypeDialog>({ withId(id) }) {
  
  val passwordEntry = KEntryTypeItemView(id, R.drawable.ic_lock, "Password")
  val creditCardEntry = KEntryTypeItemView(id, R.drawable.ic_credit_card, "Debit or credit card")
  val plainTextEntry = KEntryTypeItemView(id, R.drawable.ic_plain_text, "Plain text")
  
  class KEntryTypeItemView(
    private val bottomSheetId: Int,
    private val iconRes: Int,
    private val text: String,
  ) : KBaseView<KEntryTypeItemView>({
    isInstanceOf(LinearLayout::class.java)
    isDescendantOfA { withId(bottomSheetId) }
    withDescendant { withDrawable(iconRes) }
    withDescendant { withText(text) }
  })
}
