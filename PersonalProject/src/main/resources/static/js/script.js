function verify() {
var password1 = document.forms['form']['password'].value;
var password2 = document.forms['form']['verifyPassword'].value;
if (password1 == null || password1 == "" || password1 != password2) {
document.getElementById("error").innerHTML = "Please check your passwords";
return false;
}
}
function getGl(id) {
    if (document.getElementById("gl" + id).innerHTML == "") {
        fetch('http://localhost:8080/getGoal/' + id) // Correct URL
            .then(gl => gl.json())
            .then(function(gl) {
                var textToDisplay = "";
                textToDisplay += "Name: " + gl.goalName + "<br>";
                textToDisplay += "Goal Amount: " + gl.goalAmount + "<br>";
                textToDisplay += "Current Amount: " + gl.currentAmount + "<br>";
                document.getElementById("gl" + id).innerHTML = textToDisplay;
            })
    }
}
