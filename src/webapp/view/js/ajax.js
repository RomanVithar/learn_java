/* Article FructCode.com */
$(document).ready(function () {
	$("#btn").click(
		function () {
			sendAjaxForm('ajax_form', 'http://');
			return false;
		}
	);
});

function sendAjaxForm(ajax_form, url) {
	$.ajax({
		url: url, //url страницы
		type: "POST", //метод отправки
		dataType: "html", //формат данных
		data: $("#" + ajax_form).serialize(), // Сеарилизуем объект
		error: function (response) { // Данные не отправлены
			$('#result_form').html('Ошибка. Данные не отправлены.');
		}
	});
};

/*получиение инфы */
var btnResponse = document.getElementById("btnResponse");
btnResponse.addEventListener("click", function () {
	$.ajax({
		url: 'http://www.mocky.io/v2/5944e07213000038025b6f30' // url сервера
	}).then(function (result) {
		console.log('result', result)
	}).catch(function (err) {
		console.log('err', err)
	})
});
