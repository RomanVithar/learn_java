/* Article FructCode.com */
$(document).ready(function () {
	$("#btn").click(
		function () {
			sendAjaxForm('ajax_form', 'http://localhost:8081/sendData');
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
		url: 'http://localhost:8081/getOwners', // url сервера
	}).then(function (result) {
		let list = JSON.parse(result);
		$elem = document.querySelector('#container');
		for (let i;i<list.length;i++){
			$elem.appendChild(list[i])
		}

	}).catch(function (err) {
		console.log('err', err)
	})
});
