package com.arsvechkarev.vault.features.info

import android.os.Bundle
import android.view.Gravity.CENTER
import android.view.Gravity.CENTER_HORIZONTAL
import android.view.Gravity.NO_GRAVITY
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.AndroidThreader
import com.arsvechkarev.vault.core.Singletons.passwordsListCachingRepository
import com.arsvechkarev.vault.core.extensions.moxyPresenter
import com.arsvechkarev.vault.core.extensions.setSoftInputMode
import com.arsvechkarev.vault.core.model.ServiceInfo
import com.arsvechkarev.vault.core.navigation.Screen
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.DividerHeight
import com.arsvechkarev.vault.viewbuilding.Dimens.ImageServiceNameSize
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginBig
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginDefault
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginMedium
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.ImageBack
import com.arsvechkarev.vault.viewbuilding.TextSizes
import com.arsvechkarev.vault.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.vault.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.vault.viewdsl.Size.IntSize
import com.arsvechkarev.vault.viewdsl.animateInvisible
import com.arsvechkarev.vault.viewdsl.animateVisible
import com.arsvechkarev.vault.viewdsl.backgroundColor
import com.arsvechkarev.vault.viewdsl.classNameTag
import com.arsvechkarev.vault.viewdsl.gravity
import com.arsvechkarev.vault.viewdsl.image
import com.arsvechkarev.vault.viewdsl.invisible
import com.arsvechkarev.vault.viewdsl.layoutGravity
import com.arsvechkarev.vault.viewdsl.marginHorizontal
import com.arsvechkarev.vault.viewdsl.margins
import com.arsvechkarev.vault.viewdsl.onClick
import com.arsvechkarev.vault.viewdsl.tag
import com.arsvechkarev.vault.viewdsl.text
import com.arsvechkarev.vault.viewdsl.textColor
import com.arsvechkarev.vault.viewdsl.textSize
import com.arsvechkarev.vault.views.EditableTextInfoViewGroup
import com.arsvechkarev.vault.views.PasswordActionsView
import com.arsvechkarev.vault.views.dialogs.LoadingDialog
import com.arsvechkarev.vault.views.dialogs.loadingDialog
import com.arsvechkarev.vault.views.drawables.LetterInCircleDrawable

class InfoScreen : Screen(), InfoView {
  
  override fun buildLayout() = withViewBuilder {
    RootCoordinatorLayout {
      ScrollableVerticalLayout {
        gravity(CENTER_HORIZONTAL)
        ImageView(WrapContent, WrapContent, style = ImageBack) {
          layoutGravity(NO_GRAVITY)
          onClick { navigator.popCurrentScreen() }
        }
        ImageView(ImageServiceNameSize, ImageServiceNameSize) {
          tag(ImageServiceName)
        }
        TextView(WrapContent, WrapContent, style = BaseTextView) {
          text(R.string.text_service_name)
          margins(top = MarginBig)
          textColor(Colors.TextSecondary)
        }
        val editableCommonBlock: EditableTextInfoViewGroup.() -> Unit = {
          marginHorizontal(MarginDefault)
          onEditClickAllowed = { !presenter.isEditingSomethingNow }
          onSwitchToEditMode = { presenter.switchToEditingMode() }
        }
        child<EditableTextInfoViewGroup>(MatchParent, WrapContent) {
          tag(EditableTextInfoServiceName)
          apply(editableCommonBlock)
          whenPresenterReady { onSaveClickAllowed = presenter::onServiceNameSavingAllowed }
          whenPresenterReady { onTextSaved = presenter::saveServiceName }
        }
        View(MatchParent, IntSize(DividerHeight)) {
          backgroundColor(Colors.Divider)
          marginHorizontal(MarginSmall)
          margins(top = MarginDefault)
        }
        TextView(WrapContent, WrapContent, style = BaseTextView) {
          margins(top = MarginDefault)
          textColor(Colors.TextSecondary)
          text(R.string.text_email_optional)
        }
        child<EditableTextInfoViewGroup>(MatchParent, WrapContent) {
          tag(EditableTextInfoEmail)
          apply(editableCommonBlock)
          whenPresenterReady { onTextSaved = presenter::saveEmail }
        }
        View(MatchParent, IntSize(DividerHeight)) {
          backgroundColor(Colors.Divider)
          marginHorizontal(MarginSmall)
          margins(top = MarginDefault)
        }
        TextView(WrapContent, WrapContent, style = BaseTextView) {
          text(R.string.text_password)
          margins(top = MarginDefault)
          textColor(Colors.TextSecondary)
          textSize(TextSizes.H2)
        }
        FrameLayout(WrapContent, WrapContent) {
          margins(top = MarginSmall, start = MarginDefault, end = MarginDefault)
          TextView(WrapContent, WrapContent, style = BoldTextView) {
            tag(TextPassword)
            invisible()
            layoutGravity(CENTER)
            gravity(CENTER)
            textSize(TextSizes.H1)
            textColor(Colors.Accent)
          }
          TextView(WrapContent, WrapContent, style = BoldTextView) {
            tag(TextPasswordStub)
            textSize(TextSizes.PasswordStub)
            layoutGravity(CENTER)
            gravity(CENTER)
            text(R.string.text_password_stub)
          }
        }
        child<PasswordActionsView>(MatchParent, WrapContent) {
          classNameTag()
          margins(top = MarginMedium)
          onEditClick { presenter.onEditPasswordIconClicked() }
          whenPresenterReady { onTogglePassword = presenter::onTogglePassword }
        }
      }
      LoadingDialog()
    }
  }
  
  private val presenter by moxyPresenter {
    InfoPresenter(passwordsListCachingRepository, AndroidThreader)
  }
  
  override fun onInit(arguments: Bundle) {
    val mode = arguments.getParcelable<ServiceInfo>(SERVICE_INFO) as ServiceInfo
    presenter.performSetup(mode)
  }
  
  override fun onRelease() {
    contextNonNull.setSoftInputMode(SOFT_INPUT_ADJUST_RESIZE)
  }
  
  override fun showPasswordEditingDialog() {
  
  }
  
  override fun showLetterChange(letter: String) {
    val image = imageView(ImageServiceName)
    if (image.drawable is LetterInCircleDrawable) {
      (image.drawable as LetterInCircleDrawable).setLetter(letter)
    } else {
      val drawable = LetterInCircleDrawable(letter)
      image.image(drawable)
    }
  }
  
  override fun showServiceName(serviceName: String) {
    viewAs<EditableTextInfoViewGroup>(EditableTextInfoServiceName).setText(serviceName)
  }
  
  override fun showEmail(email: String) {
    val editableEmail = viewAs<EditableTextInfoViewGroup>(EditableTextInfoEmail)
    editableEmail.setText(email)
    editableEmail.transferTextToEditTextWhenSwitching = true
  }
  
  override fun showNoEmail() {
    val editableEmail = viewAs<EditableTextInfoViewGroup>(EditableTextInfoEmail)
    editableEmail.setText(contextNonNull.getString(R.string.text_no_email))
    editableEmail.transferTextToEditTextWhenSwitching = false
  }
  
  override fun showErrorSavingServiceName(errorText: String) {
  }
  
  override fun setPassword(password: String) {
    textView(TextPassword).text(password)
  }
  
  override fun showPassword(password: String) {
    textView(TextPassword).text(password)
    textView(TextPassword).animateVisible()
    textView(TextPasswordStub).animateInvisible()
  }
  
  override fun hidePassword() {
    textView(TextPassword).animateInvisible()
    textView(TextPasswordStub).animateVisible()
  }
  
  override fun switchFromEditingMode() {
    viewAs<EditableTextInfoViewGroup>(EditableTextInfoEmail).setTextFromTextView()
  }
  
  override fun showLoading() {
    loadingDialog().show()
  }
  
  override fun showFinishLoading() {
    loadingDialog().hide()
  }
  
  override fun allowBackPress(): Boolean {
    return presenter.allowBackPress()
  }
  
  companion object {
    
    const val SERVICE_INFO = "SERVICE_INFO"
    
    const val ImageServiceName = "ImageServiceName"
    const val EditableTextInfoServiceName = "EditableTextInfoServiceName"
    const val EditableTextInfoEmail = "EditableTextInfoEmail"
    const val TextPassword = "TextPassword"
    const val TextPasswordStub = "TextPasswordStub"
  }
}