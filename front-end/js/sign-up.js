var password = document.getElementById("password")
var passwordConfirm = document.getElementById("passwordConfirm");

function validatePassword(){
  if(password.value != passwordConfirm.value) {
    passwordConfirm.setCustomValidity("Passwords Don't Match");
  } else {
    passwordConfirm.setCustomValidity('');
  }
}

password.onchange = validatePassword;
passwordConfirm.onkeyup = validatePassword;
