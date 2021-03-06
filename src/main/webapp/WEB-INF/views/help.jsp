<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<head>
<script src="<c:url value="/resources/script/Chart.js" />"></script>
<script src="<c:url value="/resources/script/datepicker.min.js" />"></script>
<link href="<c:url value="/resources/datepicker.min.css"/>"
	rel="stylesheet" type="text/css">
<script
	src="<c:url value="/resources/script/jquery.sumoselect.min.js" />"></script>
<link href="<c:url value="/resources/sumoselect.css"/>" rel="stylesheet"
	type="text/css">
<link rel="stylesheet"
	href="<c:url value="/resources/fonts/font-awesome-4.7.0/css/font-awesome.min.css"/>">
<link href="<c:url value="/resources/calendar.css"/>" rel="stylesheet"
	type="text/css">
<link href="<c:url value="/resources/summaryStyle.css"/>" rel="stylesheet"
	type="text/css">
</head>

<div class="menuHelp-wrap" id="menuHelp-wrap">
	<script>
		$(window).scroll(function() {
			if ($(this).scrollTop() > 60) {
				$("#menuHelp-wrap").addClass("menuHelp-wrap-fixed");
			} else {
				$("#menuHelp-wrap").removeClass("menuHelp-wrap-fixed");
			}
		});
	</script>

	<nav class="menuHelp">
		<div class="nav--section">
			<h2 class="nav--section-title">
				<a href="#intro">Описание</a>
			</h2>
		</div>
		<div class="nav--section">
			<h2 class="nav--section-title">
				<a href="#menu">Меню</a>
			</h2>
		</div>
		<div class="nav--section">
			<h2 class="nav--section-title">
				<a href="#timesheet">Учет</a>
			</h2>
			<div class="nav--subsection">
				<h3 class="nav--subsection-title">
					<a href="#sub-section-1">Недавние записи</a>
				</h3>
			</div>
			<div class="nav--subsection">
				<h3 class="nav--subsection-title">
					<a href="#sub-section-2">Новая запись</a>
				</h3>
			</div>
		</div>
		<div class="nav--section">
			<h2 class="nav--section-title">
				<a href="#projects">Проекты</a>
			</h2>
			<div class="nav--subsection">
				<h3 class="nav--subsection-title">
					<a href="#sub-section-3">Новый проект</a>
				</h3>
			</div>
			<div class="nav--subsection">
				<h3 class="nav--subsection-title">
					<a href="#sub-section-4">Изменить проект</a>
				</h3>
			</div>
		</div>
		<div class="nav--section">
			<h2 class="nav--section-title">
				<a href="#tasks">Задачи</a>
			</h2>
			<div class="nav--subsection">
				<h3 class="nav--subsection-title">
					<a href="#sub-section-5">Новая задача</a>
				</h3>
			</div>
			<div class="nav--subsection">
				<h3 class="nav--subsection-title">
					<a href="#sub-section-6">Изменить задачу</a>
				</h3>
			</div>
		</div>
		<div class="nav--section">
			<h2 class="nav--section-title">
				<a href="#employees">Сотрудники</a>
			</h2>
		</div>
		<div class="nav--section">
			<h2 class="nav--section-title">
				<a href="#stat">Статистика</a>
			</h2>
			<div class="nav--subsection">
				<h3 class="nav--subsection-title">
					<a href="#sub-section-7">Статистика сотрудника</a>
				</h3>
			</div>
			<div class="nav--subsection">
				<h3 class="nav--subsection-title">
					<a href="#sub-section-8">Статистика проекта</a>
				</h3>
			</div>
			<div class="nav--subsection">
				<h3 class="nav--subsection-title">
					<a href="#sub-section-9">Общая статистика</a>
				</h3>
			</div>
		</div>
		<div class="nav--section">
			<h2 class="nav--section-title">
				<a href="#calendar">Календарь</a>
			</h2>
			<div class="nav--subsection">
				<h3 class="nav--subsection-title">
					<a href="#sub-section-10">Календарь отпусков</a>
				</h3>
			</div>
		</div>
		<div class="nav--section">
			<h2 class="nav--section-title">
				<a href="#help">Помощь</a>
			</h2>
		</div>
		<div class="nav--section">
			<h2 class="nav--section-title">
				<a href="#account">Пользователь</a>
			</h2>
		</div>
		<div class="nav--section">
			<h2 class="nav--section-title">
				<a href="#update">Обновления</a>
			</h2>
		</div>
	</nav>
</div>

<div class="container">
	<article id="intro" class="help-article">
		<h2>Описание</h2>
		<p>Сетевая система учета рабочего времени</p>
	</article>

	<article id="menu" class="help-article">
		<h2>Меню</h2>
		<div class="menuForm">
			<ul class="menu">
				<li><a class="activeMenuItem" href="#timesheet">Учет</a></li>
				<li class="dropdown"><a class="menuItem" href="#projects">Проекты</a>
					<div id="dropdown-content">
						<a href="#sub-section-3">Новый проект</a>
					</div></li>
				<li><a class="menuItem" href="#tasks">Задачи</a></li>
				<li><a class="menuItem" href="#employees">Сотрудники</a></li>
				<li class="dropdown"><a class="menuItem" href="#stat">Статистика</a>
					<div id="dropdown-content">
						<a href="#sub-section-9">Общая статистика</a>
					</div></li>
				<li><a class="menuItem" href="#calendar">Календарь</a></li>
				<li><a class="menuItem" href="#help">Помощь</a></li>
				<li id="currentUser"><a class="menuItem" href="#account">sidorov</a></li>
			</ul>
		</div>
		<p>Главное меню содержит пункты: Учет, Проекты, Задачи,
			Сотрудники, Статистика, Календарь, Помощь и "текущий пользователь".</p>
		<p>Пункт меню "Учет" позволяет перейти на страницу, где
			отображается записи за определенную неделю. Также на странице "Учет"
			производится редактирование сохраненных записей и создание новых.</p>
		<p>Пункт меню "Проекты" позволяет перейти на страницу, где
			отображается список ваших проектов, т.е. проекты, где вы являетесь
			ведущим сотрудником. Данный пункт меню содержит подпункт "Новый
			проект", доступный только для сотрудника с повышенным уровенм
			доступа. При наведении мыши на пункт "Проекты" появится подпункт
			"Новый проект", который позволяет перейти на страницу со списком всех
			проектов и возможностью создать новый проект или удалить старый.</p>
		<p>Пункт меню "Задачи" позволяет перейти на страницу, где
			отображается список всех задач, и есть возможность создать новую
			задачу. Данный пункт отображается только у пользователей с повышенным
			доступом.</p>
		<p>Пункт меню "Сотрдуники" позволяет перейти на страницу, где
			отображается список ваших непосредственных подчиненных.</p>
		<p>Пункт меню "Статистика" позволяет перейти на страницу, где
			отображается ваша статистика по проектам и задачам.</p>
		<p>Пункт меню "Календарь" позволяет перейти на страницу, где
			отображается рабочий календарь за год.</p>
		<p>Пункт меню "Помощь" позволяет перейти на текущую странице, где
			описаны основные моменты работы с сайтом.</p>
		<p>Пункт меню "Текущий пользователь" (на картинке выше "sidorov" -
			отображается имя вашего логина), позволяет перейти на страницу с
			данными вашего аккаунта.</p>
	</article>

	<article id="timesheet" class="help-article">
		<h2>Учет</h2>
		<p>При открытии данной страницы отображается таблица с записями за
			текущую неделю.</p>
		<p>Таблица содержит следующие столбцы: дата, проект, задача, часы,
			часы, комментарий. В столбце "Дата" отображается дата и день недели
			соответствующей записи в таблице. Записи с одинаковой датой
			объединяются в столбце "Дата" и втором столбце "Часы" в одну строку.
			Столбец "Проект" содержит название проекта над которым выполнялась
			работа, соответствующая задаче в столбце "Задача". В первом столбце
			"Часы" отображается время выделенное на данную задачу. Во втором
			стоблце содержится сумма часов всех записей за один день. Столбец
			"Комментарий" содержит различные комментарии, замечания
			пользователей.</p>
		<p id="sub-section-1">В конце таблицы отображается общее
			количество часов, отработанных за текущую неделю.</p>
		<p>Данная страница ялвяется страницей по умолчанию (после ввода
			логина и пароля).</p>
		<div class="listTimesheet">
			<div>
				<div style="float: left;">
					<h3>1 неделя 2017 года: 26 декабря - 01 января</h3>
				</div>
				<div id="saveCurrentTimesheet">
					<input type="submit" value="Сохранить квартал" />
				</div>
			</div>
			<div class="example">
				<table class="timesheetTable">
					<thead>
						<tr>
							<th scope="col" class="colDate">Дата</th>
							<th scope="col" class="colProject">Проект</th>
							<th scope="col" class="colTask">Задача</th>
							<th scope="col" class="colCount"><i
								class="fa fa-clock-o fa-1x" aria-hidden="true"
								title="Время выделенное на задачу, часы"></i></th>
							<th scope="col" class="colHours"><i
								class="fa fa-clock-o fa-1x" aria-hidden="true"
								title="Общее время работы за день, часы"></i></th>
							<th scope="col" class="colOverHours"><i
								class="fa fa-clock-o fa-1x" aria-hidden="true"
								title="Переработки/опоздания за день, часы"></i></th>
							<th scope="col" class="colComment">Комментарий</th>
						</tr>
					</thead>
					<tbody>
						<tr class="odd">
							<td rowspan="1">понедельник <br /> 26-12-2016
							</td>
							<td>САУ ДГУ 1 блок ЛАЭС-2</td>
							<td>Разработка эксплуатационной документации</td>
							<td>8.0</td>
							<td rowspan="1">8.0</td>
							<td rowspan="1">0.0</td>
							<td>Замечания к ПМ ТЗБиС</td>
							<td id="colLast"><input type="submit" value="Удалить"
								onClick="return confirm('Удалить запись?')" /></td>
						</tr>
						<tr class="even">
							<td rowspan="2">вторник <br /> 27-12-2016
							</td>
							<td>САУ ДГУ 1 блок ЛАЭС-2</td>
							<td>Разработка эксплуатационной документации</td>
							<td>8.0</td>
							<td rowspan="2">9.0</td>
							<td rowspan="2" class="timesheet-overcount">1.0</td>
							<td>Замечания к ПМ САР</td>
							<td id="colLast"><input type="submit" value="Удалить"
								onClick="return confirm('Удалить запись?')" /></td>
						</tr>
						<tr class="even">
							<td>САУ ДГУ 1 блок ЛАЭС-2</td>
							<td>ПНР</td>
							<td>1.0</td>
							<td></td>
							<td id="colLast"><input type="submit" value="Удалить"
								onClick="return confirm('Удалить запись?')" /></td>
						</tr>
						<tr class="odd">
							<td rowspan="1">среда <br /> 28-12-2016
							</td>
							<td>САУ ДГУ 1 блок ЛАЭС-2</td>
							<td>ПНР</td>
							<td>1.0</td>
							<td rowspan="1">1.0</td>
							<td rowspan="1" class="timesheet-abovecount">-7.0</td>
							<td></td>
							<td id="colLast"><input type="submit" value="Удалить"
								onClick="return confirm('Удалить запись?')" /></td>
						</tr>
						<tr class="even">
							<td rowspan="2">четверг <br /> 29-12-2016
							</td>
							<td>АСКРО СХК</td>
							<td>ПО ПЛК</td>
							<td>2.0</td>
							<td rowspan="2">3.0</td>
							<td rowspan="2" class="timesheet-abovecount">-4.0</td>
							<td></td>
							<td id="colLast"><input type="submit" value="Удалить"
								onClick="return confirm('Удалить запись?')" /></td>
						</tr>
						<tr class="even">
							<td>САУ ДГУ 1 блок ЛАЭС-2</td>
							<td>ПНР</td>
							<td>1.0</td>
							<td></td>
							<td id="colLast"><input type="submit" value="Удалить"
								onClick="return confirm('Удалить запись?')" /></td>
						</tr>
						<tr class="odd">
							<td rowspan="1">пятница <br /> 30-12-2016
							</td>
							<td>САУ ДГУ 1 блок ЛАЭС-2</td>
							<td>ПО ПЛК</td>
							<td>8.0</td>
							<td rowspan="1">8.0</td>
							<td rowspan="1">0.0</td>
							<td></td>
							<td id="colLast"><input type="submit" value="Удалить"
								onClick="return confirm('Удалить запись?')" /></td>
						</tr>
						<tr class="even">
							<td rowspan="1" class="timesheet-weekend">суббота <br />
								31-12-2016
							</td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
						</tr>
						<tr class="odd">
							<td rowspan="1" class="timesheet-weekend">воскресенье <br />
								01-01-2017
							</td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
						</tr>
					</tbody>
					<tfoot>
						<tr>
							<td style="text-align: right;" colspan="4">Общее количество
								времени работы, ч:</td>
							<td colspan="3">29.0</td>
						</tr>
						<tr>
							<td style="text-align: right;" colspan="4">Общее количество
								времени переработок, ч:</td>
							<td colspan="3">-10.0</td>
						</tr>
					</tfoot>
				</table>
				<div id="moreUrl">
					<a href="#">Предыдующая неделя</a> <a style="float: right;"
						href="#">Следующая неделя</a>
				</div>
			</div>
		</div>
		<p>Значение в первом столбце "Часы" доступно для изменения.
			Необходимо выделить значение, ввести новое и нажать кнопку "Enter" на
			клавиатуре, при успешном изменении значение обновится, и
			пересчитаются значения во втором ("Общие часы") и втретьем
			(Переработки) столбцах.</p>
		<p>Текст в столбце "Комментарий" также доступен для изменения.
			Необходимо ввести новый текст, и после нажатия кнопки "Enter" на
			клавиатуре - текст обновится.</p>
		<p>Дата в первом столбце выделяется красным цветом, если этот день
			выходной. Желтым если этот день короткий.</p>
		<p>
			Кнопка <input type="submit" value="Удалить"
				onClick="return confirm('Удалить запись?')" /> позволяет удалить
			запись в соответствующей строке.
		</p>
		<p>
			Для сохранения записей учета за текущий* квартал в файл .xls
			необходимо нажать кнопку <input type="submit"
				value="Сохранить квартал" /> и откроется стандартное окно
			сохранения файла.
		</p>
		<p>
			Для перехода к предыдущей или следующей недели используйте ссылки <a
				href="#">Предыдующая неделя</a> и <a href="#">Следующая неделя</a>
			соответственно.
		</p>

		<div id="sub-section-2">
			<p>
				Для добавления новой записи необходимо заполнить форму "Новая
				запись" и нажать кнопку <input type="submit" value="Добавить"
					class="buttonAdd" />.
			</p>
			<div class="timesheetForm">
				<h3>Новая запись</h3>
				<form id="timesheetForm" onSubmit="return validate()" action=""
					method="POST">
					<table class="newRecordTable">
						<thead>
							<tr>
								<th class="colDate">Дата</th>
								<th class="colProject">Проект</th>
								<th class="colTask">Задача</th>
								<th class="colCount">Часы</th>
								<th class="colComment">Комментарий</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td><input id="datepickerTS" name="dates"
									data-position="bottom left" data-multiple-dates-separator=", "
									type="text" class="datepicker-here input" required="required"
									data-multiple-dates="true" value="" /></td>
								<td><select id="selectProject" name="projectId"
									class="input" required="required">
										<option value="">--Выберите проект</option>
										<option value="1">САУ ДГУ 1 блок ЛАЭС-2</option>
										<option value="2">АСКРО СХК</option>
										<option value="3">СХМ ВХР ЛАЭС2</option>
										<option value="4">Администрирование</option>
										<option value="5">АПТС-Онега</option>
										<option value="6">Метро</option>
								</select></td>
								<td><select id="selectTask" name="taskId" class="input"
									required="required">
										<option value="">--Выберите задачу</option>
										<option value="1">Разработка технического задания</option>
										<option value="2">Разработка спецификации</option>
										<option value="3">ПО ПЛК</option>
										<option value="4">ПО ВУ</option>
										<option value="5">Разработка эксплуатационной
											документации</option>
										<option value="6">Формирование ИД</option>
										<option value="7">Обработка ИД</option>
										<option value="8">ПНР</option>
										<option value="9">ПСИ</option>
										<option value="11">Установка и настройка ПО</option>
										<option value="12">Разработка деталей и блоков</option>
										<option value="14">Разное</option>
								</select></td>
								<td><input id="inputCountTime" name="countTime" type="text"
									list="listTimes" class="input" required="required" value="0.0" />
									<datalist id="listTimes">
										<option value="0.5" />
										<option value="1.0" />
										<option value="2.0" />
										<option value="4.0" />
										<option value="8.0" />
									</datalist></td>
								<td><input id="comment" name="comment" type="text"
									class="input" value="" /></td>
							</tr>
						</tbody>
						<tfoot></tfoot>
					</table>

					<div>
						<input type="submit" value="Добавить" class="buttonAdd" />
						<p class="errorMessage" id="errorValidate"
							style="display: inline-block;"></p>
					</div>
				</form>
			</div>
			<p>Форма "Новая запись" содержит пять полей: Дата, Проект,
				Задача, Часы и Комментарий.</p>
			<p>Все поля, кроме поля "Комментарий", обязательно требуется
				заполнить для сохранения записи.</p>
			<p>Поле "Дата" заполняется в формате DD-MM-YYYY, где DD - день
				месяца, MM - месяц, YYYY - год. Если необходимо создать несколько
				одинаковых записей для различных дней, необходимо перечеслить даты
				через запятую, например: "24-01-2016, 25-01-2016" (без кавычек).
				Проще всего воспользоваться календарем и выбрать необходимые даты.
				Календарь автоматически отображается при щелчке левой кнопки мыши в
				поле "Дата".</p>
			<p>В поле "Проект" вводится название проекта, над которым
				производилась работа. Можно выбрать название проекта из выпадающего
				списка, или начать ввод для фильтрации по названию. Если требуемого
				проекта нет в списке и у вас нет доступа для добавления нового
				проекта, то вам необходимо обратиться к вашему непосредственному
				руководителю.</p>
			<p>В поле "Задача" выбирается название выполняемой задачи. Также
				как и название проекта, можно выбрать название задачи из выпадающего
				списка, или начать ввод для фильтрации по назаванию. Если требуемой
				задачи нет в списке и у вас нет доступа для добавления новой задачи,
				то вам необходимо обратиться к вашему непосредственному
				руководителю.</p>
			<p>В поле "Часы" вводится количество выделенных часов для данной
				задачи.</p>
			<p>Поле "Комментарий" не требуется для обязтаельного заполнения.
				Может использоваться пользователями в различных целях, например, для
				уточнения выполненной задачи или т.п.</p>
		</div>
	</article>

	<article id="projects" class="help-article">
		<h2>Проекты</h2>
		<p>При открытии данной страницы отображается таблица со списком
			проектов, в которых вы являетесь ведущим сотрудником.</p>
		<p>Дополнительно в таблице отображается статус проекта и все
			ведущие сотрудники проекта.</p>
		<div class="divWithBorder">
			<h3>Мои проекты</h3>
			<table class="mainTable">
				<thead>
					<tr>
						<th class="colProjectName">Проект</th>
						<th class="colProjectContract">Номер договора</th>
						<th class="colProjectActive">Статус</th>
						<th class="colProjectLeaders">Ведущие сотрудники</th>
						<th class="colProjectComment">Комментарий</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>САУ ДГУ</td>
						<td>123</td>
						<td>В работе</td>
						<td>Сидоров Федор, Петров Иван</td>
						<td></td>
					</tr>
					<tr>
						<td>САУ ВО</td>
						<td>321САУ ВО</td>
						<td>В работе</td>
						<td>Сидоров Федор, Соболев Николай</td>
						<td></td>
					</tr>
				</tbody>
				<tfoot></tfoot>
			</table>
		</div>
		<p>
			Для перехода на страницу <a href="#sub-section-8">статистики
				проекта</a>, необходимо щелкнуть левой кнопкой мыши на строке,
			соответствующего проекта.
		</p>
	</article>

	<article id="sub-section-3" class="help-article">
		<h3>Новый проект</h3>
		<p>
			У сотрудников с повышенным уровнем доступа есть возможность создавать
			новый проект. Для этого необходимо перейти на страницу "Новый
			проект", выбрав пункт "Новый проект" в главном меню (см. раздел <a
				href="#menu">Меню</a>).
		</p>

		<div class="projectForm">
			<h3>Новый проект</h3>
			<form id="projectForm" action="" method="POST">
				<table class="newRecordTable">
					<thead>
						<tr>
							<th class="colProjectName">Проект</th>
							<th class="colProjectContract">Номер договора</th>
							<th class="colProjectActive">Статус</th>
							<th class="colProjectLeaders">Ведущие сотрудники</th>
							<th class="colProjectComment">Комментарий</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td><input required="required" class="input" type="text"
								name="name" /></td>
							<td><input class="input" type="text" name="contract" /></td>
							<td><select id="selectStatus" name="status">
									<option value="1">В работе</option>
									<option value="2">Завершен</option>
							</select></td>
							<td><select id="selectManagers" name="projectLeaders"
								multiple="multiple">
									<option value="1">Сидоров Федор Петрович</option>
									<option value="2">Петров Иван Генадьевич</option>
									<option value="3">Соболев Николай Альбертович</option>
							</select><input type="hidden" name="_projectLeaders" value="1" /></td>
							<td><input id="comment" name="comment" type="text"
								class="input" value="" /></td>
						</tr>
					</tbody>
					<tfoot></tfoot>
				</table>
				<input type="submit" value="Добавить" class="buttonAdd" />
			</form>
		</div>
		<p>
			Для создания нового проекта необходимо заполнить форму "Новый проект"
			и нажать кнопку <input type="submit" value="Добавить"
				class="buttonAdd" />.
		</p>
		<p>Форма "Новый проект" содержит четыре поля: Проект, Статус,
			Ведущие сотрудники, Комментарий.</p>
		<p>Поле "Проект" содержит название проекта и должно быть уникально
			для всех проектов.</p>
		<p>Поле "Статус" содержит статус проекта. Проект находится в двух
			стадиях: актив (выполняется) и завершен (работы не ведутся).
			Завершенные проекты не отображаются в списке проектов в форме "Новая
			запись" на странице "Учет" (значит сотрудники не могут добавлять
			записей для этого проекта).</p>
		<p>Поле "Ведущие сотрудники" - сотрудники, которые могут
			просматривать статистику по данному проекту.</p>
		<p>Поле "Комментарий" не обязательно для заполнения. Используется
			для различных целей, например, указание объекта (станции) или другой
			важной информации для данного проекта.</p>
		<p>На странице "Новый проект" также отображается список всех
			проектов фирмы с возможностью удаления или изменения выбранного
			проекта. Возможно удалить только проект не имеющий ни одной записи в
			учете. При попытке удалить проект с записями - приложение выдаст
			ошибку.</p>
		<div class="divWithBorder">
			<h3>Все проекты</h3>
			<table class="mainTable">
				<thead>
					<tr>
						<th class="colProjectName">Проект</th>
						<th class="colProjectContract">Номер договора</th>
						<th class="colProjectActive">Статус</th>
						<th class="colProjectLeaders">Ведущие сотрудники</th>
						<th class="colProjectComment">Комментарий</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>САУ ДГУ</td>
						<td>123</td>
						<td>В работе</td>
						<td>Сидоров Федор, Петров Иван</td>
						<td></td>
						<td id="colLast">
							<button class="btn btn-primary">Изменить</button>
						</td>
						<td id="colLast">
							<button class="btn btn-primary">Удалить</button>
						</td>
					</tr>
					<tr>
						<td>САУ ВО</td>
						<td>321САУ ВО</td>
						<td>В работе</td>
						<td>Сидоров Федор, Соболев Николай</td>
						<td></td>
						<td id="colLast">
							<button class="btn btn-primary">Изменить</button>
						</td>
						<td id="colLast">
							<button class="btn btn-primary">Удалить</button>
						</td>
					</tr>
				</tbody>
				<tfoot></tfoot>
			</table>
		</div>
	</article>

	<article id="sub-section-4" class="help-article">
		<h3>Изменить проект</h3>
		<p>
			У сотрудников с повышенным уровнем доступа есть возможность изменить
			атрибуты проекта. Для этого необходимо перейти на страницу "Новый
			проект", и нажать кнопку
			<button class="btn btn-primary">Изменить</button>
			напротив соответствующего проекта. Откроется страница для изменения
			выбранного проекта.
		</p>
		<div class="projectForm">
			<h3>Изменить проект</h3>
			<form id="projectForm" action="" method="POST">
				<table class="newRecordTable">
					<thead>
						<tr>
							<th class="colProjectName">Проект</th>
							<th class="colProjectContract">Номер договора</th>
							<th class="colProjectActive">Статус</th>
							<th class="colProjectLeaders">Ведущие сотрудники</th>
							<th class="colProjectComment">Комментарий</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td><input required="required" class="input" type="text"
								name="name" value="САУ ДГУ ЛАЭС" /></td>
							<td><input class="input" type="text" name="contract" /></td>
							<td><select id="selectStatusUpdate" name="status">
									<option value="1">В работе</option>
									<option value="2">Завершен</option>
							</select></td>
							<td><select id="selectManagersUpdate" name="projectLeaders"
								multiple="multiple">
									<option selected value="1">Сидоров Федор Петрович</option>
									<option value="2">Петров Иван Генадьевич</option>
									<option value="3">Соболев Николай Альбертович</option>
							</select></td>
							<td><input id="comment" name="comment" type="text"
								class="input" value="" /></td>
						</tr>
					</tbody>
					<tfoot></tfoot>
				</table>
				<input type="submit" value="Обновить" class="buttonAdd" />
			</form>
		</div>
		<p>
			После изменения атрибутов проекта необходимо нажать кнопку <input
				type="submit" value="Обновить" class="buttonAdd" /> для сохранения
			изменений.
		</p>
	</article>

	<article id="tasks" class="help-article">
		<h2>Задачи</h2>
		<p>На странице "Задачи" отображается таблица со списком всех
			созданных задач с возможность удаления или изменения. Возможно
			удалить только задачу не имеющую ни одной записи в учете. При попытке
			удалить задачу с записями - приложение выдаст ошибку.</p>
		<p>Возможность удаления или изменения задач доступно только
			сотрудникам с повышенным уровнем доступа.</p>
		<div class="divWithBorder">
			<h3>Все задачи</h3>
			<table class="mainTable">
				<thead>
					<tr>
						<th class="colTaskName">Задача</th>
						<th class="colTaskActive">Статус</th>
						<th class="colTaskComment">Комментарий</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>Разработка технического задания</td>
						<td>Активная</td>
						<td></td>
						<td id="colLast">
							<button class="btn btn-primary">Изменить</button>
						</td>
						<td id="colLast">
							<form action="#" method="POST">
								<input type="submit" value="Удалить"
									onClick="return confirm('Удалить задачу?')" />
							</form>
						</td>
					</tr>
					<tr>
						<td>Разработка спецификации</td>
						<td>Активная</td>
						<td></td>
						<td id="colLast">
							<button class="btn btn-primary">Изменить</button>
						</td>
						<td id="colLast">
							<form action="#" method="POST">
								<input type="submit" value="Удалить"
									onClick="return confirm('Удалить задачу?')" />
							</form>
						</td>
					</tr>
					<tr>
						<td>ПО ПЛК</td>
						<td>Активная</td>
						<td></td>
						<td id="colLast">
							<button class="btn btn-primary">Изменить</button>
						</td>
						<td id="colLast">
							<form action="#" method="POST">
								<input type="submit" value="Удалить"
									onClick="return confirm('Удалить задачу?')" />
							</form>
						</td>
					</tr>
					<tr>
						<td>ПО ВУ</td>
						<td>Неактивная</td>
						<td></td>
						<td id="colLast">
							<button class="btn btn-primary">Изменить</button>
						</td>
						<td id="colLast">
							<form action="#" method="POST">
								<input type="submit" value="Удалить"
									onClick="return confirm('Удалить задачу?')" />
							</form>
						</td>
					</tr>
					<tr>
						<td>Разработка эксплуатационной документации</td>
						<td>Активная</td>
						<td></td>
						<td id="colLast">
							<button class="btn btn-primary">Изменить</button>
						</td>
						<td id="colLast">
							<form action="#" method="POST">
								<input type="submit" value="Удалить"
									onClick="return confirm('Удалить задачу?')" />
							</form>
						</td>
					</tr>
				</tbody>
				<tfoot></tfoot>
			</table>
		</div>
	</article>

	<article id="sub-section-5" class="help-article">
		<h3>Новая задача</h3>
		<p>
			Для создания новой задачи необходимо заполнить форму "Новая задача" и
			нажать кнопку <input type="submit" class="buttonAdd" value="Добавить" />.
		</p>
		<p>Данная форма видна только сотрудникам с повышенным уровнем
			доступа.</p>
		<div class="taskForm">
			<h3>Новая задача</h3>
			<form id="taskForm" action="#tasks" method="POST">
				<table class="newRecordTable">
					<thead>
						<tr>
							<th class="colTaskName">Задача</th>
							<th class="colTaskActive">Статус</th>
							<th class="colTaskComment">Комментарий</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td><input class="input" type="text" name="name" /></td>
							<td><select id="selectStatusTask" name="status">
									<option value="1">Активная</option>
									<option value="2">Неактивная</option>
							</select></td>
							<td><input id="comment" name="comment" type="text"
								class="input" value="" /></td>
						</tr>
					</tbody>
					<tfoot></tfoot>
				</table>
				<input type="submit" class="buttonAdd" value="Добавить" />
			</form>
		</div>
		<p>Форма "Новая задача" содержит три поля: Задача, Статус,
			Комментарий.</p>
		<p>Поле "Задача"" содержит название задачи и должно быть уникально
			для всех задач.</p>
		<p>Поле "Статус" содержит статус данной задачи. Возможны два
			статуса: активный, неактивный. Неактивные задачи не отображаются в
			списке задач в форму "Новая запись" на странице "Учет" (значит
			сотрудники не могут добавлять записей для этой задачи).</p>
		<p>Поле "Комментарий" не обязательно для заполнения. Используется
			для различных целей.</p>

		<p>
	</article>

	<article id="sub-section-6" class="help-article">
		<h3>Изменить задачу</h3>
		<p>
			У сотрудников с повышенным уровнем доступа есть возможность изменить
			атрибуты задачи. Для этого необходимо перейти на страницу "Задачи", и
			нажать кнопку
			<button class="btn btn-primary">Изменить</button>
			напротив соответствующей задачи. Откроется страница для изменения
			выбранной задачи.
		</p>
		<div class="projectForm">
			<h3>Изменить задачу</h3>
			<form id="taskForm" action="" method="POST">
				<table class="newRecordTable">
					<thead>
						<tr>
							<th class="colTaskName">Задача</th>
							<th class="colTaskActive">Статус</th>
							<th class="colTaskComment">Комментарий</th>

						</tr>
					</thead>
					<tbody>
						<tr>
							<td><input required="required" class="input" type="text"
								name="name" value="ПНР" /></td>
							<td><select id="selectStatusTaskUpdate" name="status">
									<option value="1">Активная</option>
									<option value="2">Неактивная</option>
							</select></td>
							<td><input id="comment" name="comment" type="text"
								class="input" value="Пусконаладочные работы" /></td>
						</tr>
					</tbody>
					<tfoot></tfoot>
				</table>
				<input type="submit" value="Обновить" class="buttonAdd" />
			</form>
		</div>
		<p>
			После изменения атрибутов задачи необходимо нажать кнопку <input
				type="submit" value="Обновить" class="buttonAdd" /> для сохранения
			изменений.
		</p>
	</article>

	<article id="employees" class="help-article">
		<h2>Сотрудники</h2>
		<p>На данной странице отображается таблица со списком всех ваших
			непосредственных подчиненных.</p>
		<div class="divWithBorder">
			<h3>Сотрудники</h3>
			<table class="mainTable">
				<thead>
					<tr>
						<th>Имя пользователя</th>
						<th>Имя</th>
						<th>Фамилия</th>
						<th>Адрес почты</th>
						<th>Должность</th>
						<th>Отдел</th>
					</tr>
				</thead>
				<tbody>

					<tr class="clickable-row" data-url="#">
						<td>sidorov</td>
						<td>Федор Петрович</td>
						<td>Сидоров</td>
						<td>sidorov@west-e.ru</td>
						<td>Инженер-программист</td>
						<td>ОПиК</td>
						<td id="colLast">
							<button class="btn btn-primary">Учет</button>
						</td>
					</tr>

					<tr class="clickable-row" data-url="#">
						<td>petrov</td>
						<td>Иван Генадьевич</td>
						<td>Петров</td>
						<td>petrov@west-e.ru</td>
						<td>Инженер-программист 3 кат.</td>
						<td>ОПиК</td>
						<td id="colLast">
							<button class="btn btn-primary">Учет</button>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<p>Для перехода на страницу статистики сотрудника, необходимо
			щелкнуть левой кнопкой мыши на строке, соответствующего сотрудника.</p>
		<p>
			Для просмотра записей учета времени выбранного сотрудника в виде
			таблицы, нажмите кнопку
			<button class="btn btn-primary">Учет</button>
			.
		</p>
	</article>

	<article id="stat" class="help-article">
		<h2 id="sub-section-7">Статистика</h2>
		<h3>Статистика сотрудника</h3>
		<p>На данной странице отображается статистика сотрудника за
			выбранный период времени.</p>
		<p>В левой части расположены параметры статистики, позволяющие
			отфильтровать или изменить тип статистики.</p>
		<p>Возможно отображение трех типов статистики: по проектам, по
			задачам или по времени.</p>
		<p>Статистика по проектам - отображает все проекты и количество
			часов, затраченные на соответствующий проект. Если нужно отобразить
			статистику часов по проектам, но по определенным задачам, то
			необходимо в фильтре снять галочку "Все задачи" и выбрать необходимые
			задачи.</p>
		<p>Статистика по задачам - отображает все задачи и количество
			часов, затраченные на соответствующую задачу. Возможно отфильтровать
			часы по проектам, для этого необходимо снять галочку "Все проекты" и
			отобрать необходимые.</p>
		<p>Статистика по времени - отображает график зависимости
			количества отработанных часов от даты.</p>
		<p>Статистика отображается за определенный период времени, который
			возможно изменить в поле "Период".</p>
		<p>
			После изменения параметров статистики необходимо обновить данные
			нажатием кнопки
			<button id="buttonRefresh">Обновить</button>
			.
		</p>

		<div>
			<nav class="filter">
				<h3>Параметры статистики</h3>
				<h4>Тип</h4>
				<select id="statType">
					<option value="1">по проектам</option>
					<option value="2">по задачам</option>
					<option value="3">по времени</option>
				</select>
				<h4>Фильтр</h4>
				<input type="checkbox" name="typeAllItems" value="true"
					id="typeAllItems" checked="checked"> <span
					id="typeAllItemsText">Все задачи</span>
				<div id="items" class="items">
					<select id="selectItems" name="selectItems" multiple="multiple">
					</select>
				</div>
				<hr />

				<h4>Период:</h4>
				<input id="statPeriod" class="statPeriod datepicker-here"
					type="text" data-position="bottom left" data-range="true"
					data-multiple-dates-separator=" - " name="statPeriod"
					value="01.01.2017 - 27.01.2017" required="required" />
				<hr />
				<button id="buttonRefresh">Обновить</button>

			</nav>

			<article>

				<div class="divWithBorder">
					<div>
						<div>
							<h3>Сидоров Федор Петрович</h3>
							<span> </span>
						</div>

						<table class="mainTable" id="tableStat">
							<thead>
								<tr>
									<th scope="col" class="colProject">Проект</th>
									<th scope="col" class="colCount">Часы</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>САУ ДГУ</td>
									<td>100</td>
								</tr>
								<tr>
									<td>АСКРО</td>
									<td>25</td>
								</tr>
								<tr>
									<td>СКУ ЭЧ</td>
									<td>50</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class="chartContainer">
						<div class="leftChart">
							<canvas id="chartByProjects" width="300" height="300"></canvas>
						</div>
						<div class="rightChart">
							<canvas id="barChartByProjects" width="600" height="300"></canvas>
						</div>
					</div>
				</div>
			</article>
		</div>
	</article>
	<article id="sub-section-8" class="help-article">
		<h3>Статистика проекта</h3>
		<p>На данной странице отображается статистика проекта за выбранный
			период времени.</p>
		<p>Данная страница доступна только ведущим проекта или
			пользователям с повышенным уровнем доступа.</p>
		<p>В левой части расположены параметры статистики, позволяющие
			отфильтровать или изменить тип статистики.</p>
		<p>Возможно отображение трех типов статистики: по сотрудникам, по
			задачам или по времени.</p>
		<p>Статистика по сотрудникам - отображает всех сотрудников и
			количество часов, затраченное соответствующим сотрудником. Если нужно
			отобразить статистику часов по сотрудникам, но по определенным
			задачам, то необходимо в фильтре снять галочку "Все задачи" и выбрать
			необходимые задачи.</p>
		<p>Статистика по задачам - отображает все задачи и количество
			часов, затраченные на соответствующую задачу. Возможно отфильтровать
			часы по сотрудникам, для этого необходимо снять галочку "Все
			сотрудники" и отобрать необходимых.</p>
		<p>Статистика по времени - отображает график зависимости
			количества наработанных часов от даты.</p>
		<p>Статистика отображается за определенный период времени, который
			возможно изменить в поле "Период".</p>
		<p>
			После изменения параметров статистики необходимо обновить данные
			нажатием кнопки
			<button id="buttonRefreshPrj">Обновить</button>
			.
		</p>
		<div>
			<nav class="filter">
				<h3>Параметры статистики</h3>
				<h4>Тип</h4>
				<select id="statTypePrj">
					<option value="1">по сотрудникам</option>
					<option value="2">по задачам</option>
					<option value="3">по времени</option>
				</select>
				<h4>Фильтр</h4>
				<input type="checkbox" name="typeAllItemsPrj" value="true"
					id="typeAllItemsPrj" checked="checked"> <span
					id="typeAllItemsPrjText">Все задачи</span>
				<div id="itemsPrj" class="items">
					<select id="selectItemsPrj" name="selectItemsPrj"
						multiple="multiple">
					</select>
				</div>
				<hr />

				<h4>Период:</h4>
				<input id="statPeriodPrj" class="statPeriod datepicker-here"
					type="text" data-position="bottom left" data-range="true"
					data-multiple-dates-separator=" - " name="statPeriod"
					value="01.01.2017 - 27.01.2017" required="required" />
				<hr />
				<button id="buttonRefreshPrj">Обновить</button>
			</nav>

			<article>

				<div class="divWithBorder">
					<div>
						<div>
							<h3>САУ ДГУ</h3>
							<span> </span>
						</div>
						<table class="mainTable" id="tableStatPrj">
							<thead>
								<tr>
									<th scope="col" class="colTask">Сотрудник</th>
									<th scope="col" class="colCount">Часы</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>Сидоров Федор Петрович</td>
									<td>50</td>
								</tr>
								<tr>
									<td>Соболев Николай Альбертович</td>
									<td>25</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class="chartContainer">
						<div class="leftChart">
							<canvas id="chartBy" width="300" height="300"></canvas>
						</div>
						<div class="rightChart">
							<canvas id="barChartBy" width="600" height="300"></canvas>
						</div>
					</div>
				</div>

			</article>

		</div>
	</article>

	<article id="sub-section-9" class="help-article">
		<h3>Общая статистика</h3>
		<p>На данной странице отображается статистика в виде таблицы по
			всем сотрудникам и проектам за выбранный период времени.</p>
		<p>Статистика отображается за определенный период времени, который
			возможно изменить в поле "Период".</p>
		<p>Статистику можно сохранить в файл Excel, нажав кнопку "Сохранить статистику".</p>
		<div id="paramsContainer">
			<div id="filterContainer">
				<div>
					<div id="filterInnerContainer">
						<label>Период: </label> <input id="statPeriod"
							class="datepicker-here" type="text" data-position="bottom left"
							data-range="true" data-multiple-dates-separator=" - "
							name="statPeriod" value="01.02.2018 - 19.02.2018"
							required="required" />
						<button id="buttonRefresh">Обновить</button>
					</div>
				</div>
			</div>

			<div id="saveStatistic">
				<form action="stat/xls" method="GET">
					<input name="statPeriod" value="01.02.2018 - 19.02.2018"
						type="hidden" /> <input name="summaryData" value="" type="hidden" />
					<input type="submit" value="Сохранить статистику" />
				</form>
			</div>
		</div>

		<div id="summary_here">
			<div class="summaryContainer" style="height: 245px;width: 625px;">
				<div class="summaryGrid" role="treegrid"
					style="width: 200px; height: 245px;">
					<div class="summaryGridScale" role="row"
						style="width: 200px; height: 34px; line-height: 33px;">
						<div class="summaryGridHeadCell summaryGridHeadText"
							style="width: 200px;">Сотрудник</div>
					</div>
					<div class="summaryGridData" role="rowgroup"
						style="width: 200px; height: 210px;">
						<div class="summaryRow" role="row"
							style="height: 35px; line-height: 35px;">
							<div class="SummaryCell" role="gridcell">
								<div class="summaryTreeContent">Андреев М. А.</div>
							</div>
						</div>
						<div class="summaryRow" role="row"
							style="height: 35px; line-height: 35px;">
							<div class="SummaryCell" role="gridcell">
								<div class="summaryTreeContent">Гулько А. Г.</div>
							</div>
						</div>
						<div class="summaryRow" role="row"
							style="height: 35px; line-height: 35px;">
							<div class="SummaryCell" role="gridcell">
								<div class="summaryTreeContent">Донцов О. А.</div>
							</div>
						</div>
						<div class="summaryRow" role="row"
							style="height: 35px; line-height: 35px;">
							<div class="SummaryCell" role="gridcell">
								<div class="summaryTreeContent">Иголкин С. А.</div>
							</div>
						</div>
						<div class="summaryRow" role="row"
							style="height: 35px; line-height: 35px;">
							<div class="SummaryCell" role="gridcell">
								<div class="summaryTreeContent">Линев А. О.</div>
							</div>
						</div>
						<div class="summaryRow" role="row"
							style="height: 35px; line-height: 35px;">
							<div class="SummaryCell" role="gridcell">
								<div class="summaryTreeContent">Соломон А. И.</div>
							</div>
						</div>
					</div>
				</div>
				<div class="summaryTask" style="width: 420px; height: 245px;">
					<div class="summaryTaskScale" style="width: 420px; height: 34px;">
						<div class="summaryScaleLine"
							style="height: 34px; position: relative; line-height: 34px;">
							<div class="summaryScaleCell" title="ПТК ИВС Армянская"
								style="width: 70px; height: 34px; position: absolute; left: 0px;">ПТК
								ИВС Армянская</div>
							<div class="summaryScaleCell"
								title="СКУ ЭЧ  эб 2 Калининской АЭС"
								style="width: 70px; height: 34px; position: absolute; left: 70px;">СКУ
								ЭЧ эб 2 Калининской АЭС</div>
							<div class="summaryScaleCell" title="САЭС АХК"
								style="width: 70px; height: 34px; position: absolute; left: 140px;">САЭС
								АХК</div>
							<div class="summaryScaleCell" title="ЛАЭС-2 СКУ СВ"
								style="width: 70px; height: 34px; position: absolute; left: 210px;">ЛАЭС-2
								СКУ СВ</div>
							<div class="summaryScaleCell" title="АСКУ ЖРО Ленинградская АЭС"
								style="width: 70px; height: 34px; position: absolute; left: 280px;">АСКУ
								ЖРО Ленинградская АЭС</div>
							<div class="summaryScaleCell" title="Итого"
								style="width: 70px; height: 34px; position: absolute; left: 350px;">Итого</div>
						</div>
					</div>
					<div class="summaryDataArea" style="width: 420px;">
						<div class="summaryTaskBg" style="width: 420px;">
							<div class="summaryTaskRow"
								style="height: 35px; line-height: 35px;">
								<div class="summaryTaskCell" style="width: 70px; height: 34px; position: absolute; left: 0px;">0.0</div>
								<div class="summaryTaskCell" style="width: 70px; height: 34px; position: absolute; left: 70px;">0.0</div>
								<div class="summaryTaskCell" style="width: 70px; height: 34px; position: absolute; left: 140px;">8.0</div>
								<div class="summaryTaskCell" style="width: 70px; height: 34px; position: absolute; left: 210px;">0.0</div>
								<div class="summaryTaskCell" style="width: 70px; height: 34px; position: absolute; left: 280px;">0.0</div>
								<div class="summaryTaskCell summaryLastCell"
									style="width: 70px; height: 34px; position: absolute; left: 350px;">8.0</div>
							</div>
							<div class="summaryTaskRow"
								style="height: 35px; line-height: 35px;">
								<div class="summaryTaskCell" style="width: 70px; height: 34px; position: absolute; left: 0px;">0.0</div>
								<div class="summaryTaskCell" style="width: 70px; height: 34px; position: absolute; left: 70px;">0.0</div>
								<div class="summaryTaskCell" style="width: 70px; height: 34px; position: absolute; left: 140px;">5.0</div>
								<div class="summaryTaskCell" style="width: 70px; height: 34px; position: absolute; left: 210px;">3.0</div>
								<div class="summaryTaskCell" style="width: 70px; height: 34px; position: absolute; left: 280px;">0.0</div>
								<div class="summaryTaskCell summaryLastCell"
									style="width: 70px; height: 34px; position: absolute; left: 350px;">8.0</div>
							</div>
							<div class="summaryTaskRow"
								style="height: 35px; line-height: 35px;">
								<div class="summaryTaskCell" style="width: 70px; height: 34px; position: absolute; left: 0px;">0.0</div>
								<div class="summaryTaskCell" style="width: 70px; height: 34px; position: absolute; left: 70px;">0.0</div>
								<div class="summaryTaskCell" style="width: 70px; height: 34px; position: absolute; left: 140px;">4.0</div>
								<div class="summaryTaskCell" style="width: 70px; height: 34px; position: absolute; left: 210px;">4.0</div>
								<div class="summaryTaskCell" style="width: 70px; height: 34px; position: absolute; left: 280px;">0.0</div>
								<div class="summaryTaskCell summaryLastCell"
									style="width: 70px; height: 34px; position: absolute; left: 350px;">8.0</div>
							</div>
							<div class="summaryTaskRow"
								style="height: 35px; line-height: 35px;">
								<div class="summaryTaskCell" style="width: 70px; height: 34px; position: absolute; left: 0px;">0.0</div>
								<div class="summaryTaskCell" style="width: 70px; height: 34px; position: absolute; left: 70px;">0.0</div>
								<div class="summaryTaskCell" style="width: 70px; height: 34px; position: absolute; left: 140px;">0.0</div>
								<div class="summaryTaskCell" style="width: 70px; height: 34px; position: absolute; left: 210px;">0.0</div>
								<div class="summaryTaskCell" style="width: 70px; height: 34px; position: absolute; left: 280px;">24.0</div>
								<div class="summaryTaskCell summaryLastCell"
									style="width: 70px; height: 34px; position: absolute; left: 350px;">24.0</div>
							</div>
							<div class="summaryTaskRow"
								style="height: 35px; line-height: 35px;">
								<div class="summaryTaskCell" style="width: 70px; height: 34px; position: absolute; left: 0px;">0.0</div>
								<div class="summaryTaskCell" style="width: 70px; height: 34px; position: absolute; left: 70px;">1.0</div>
								<div class="summaryTaskCell" style="width: 70px; height: 34px; position: absolute; left: 140px;">0.0</div>
								<div class="summaryTaskCell" style="width: 70px; height: 34px; position: absolute; left: 210px;">0.0</div>
								<div class="summaryTaskCell" style="width: 70px; height: 34px; position: absolute; left: 280px;">0.0</div>
								<div class="summaryTaskCell summaryLastCell"
									style="width: 70px; height: 34px; position: absolute; left: 350px;">1.0</div>
							</div>
							<div class="summaryTaskRow"
								style="height: 35px; line-height: 35px;">
								<div class="summaryTaskCell" style="width: 70px; height: 34px; position: absolute; left: 0px;">8.0</div>
								<div class="summaryTaskCell" style="width: 70px; height: 34px; position: absolute; left: 70px;">0.0</div>
								<div class="summaryTaskCell" style="width: 70px; height: 34px; position: absolute; left: 140px;">0.0</div>
								<div class="summaryTaskCell" style="width: 70px; height: 34px; position: absolute; left: 210px;">0.0</div>
								<div class="summaryTaskCell" style="width: 70px; height: 34px; position: absolute; left: 280px;">0.0</div>
								<div class="summaryTaskCell summaryLastCell"
									style="width: 70px; height: 34px; position: absolute; left: 350px;">8.0</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

	</article>

	<article id="calendar" class="help-article">
		<h2>Календарь</h2>
		<p>На данной странице отображен рабочий календарь на год.</p>
		<p>
			Для сотрудником с повышенным уровнем доступа доступна форма для
			обновления данных рабочего календаря. Актуальный календарь в формате
			CSV вы можете скачать <a
				href="http://data.gov.ru/opendata/7708660670-proizvcalendar">по
				ссылке</a>.
		</p>
		<div class="calendar-update">
			<h1>Обновление календаря</h1>
			<form method="POST" action="#">
				<input type="file" name="file" /> <br /> <input type="submit"
					value="Обновить" />
			</form>
		</div>
	</article>

	<article id="sub-section-10" class="help-article">
		<h3>Календарь отпусков</h3>
		<p>На данной странице отображен календарь отпусков.</p>
		<p>Каждый сотрудник может создать новый, изменить или удалить
			ранее созданный отпуск.</p>
		<p>Для создания нового отпуска необходимо нажать на кнопку "плюс"
			напротив своей фамилии. В открывшемся окне заполнить поля: дата
			начала отпуска и количество дней. Нажать кнопку "Save" для
			сохранения, или "Cancel" для отмены.</p>
		<p>Изменить отпуск можно несколькими способами: Двойным нажатием
			клавишей мыши на диаграмме отпуска, открыть окно изменения отпуска, и
			в нем изменить необходимые параметры. Нажать кнопку "Save" для
			сохранения, "Cancel" для отмены, или "Delete" для удаления.</p>
		<p>Второй способ изменения отпуска - нажатием клавиши мыши на
			диаграмме, переместить или растянуть диаграмму отпуска.</p>
		<p>Для сохранения изменений необхимо нажать кнопку "Сохранить" над
			диаграммой.</p>
	</article>

	<article id="help" class="help-article">
		<h2>Помощь</h2>
		<p>На данной странице отображено краткое описание системы.</p>
	</article>

	<article id="account" class="help-article">
		<h2>Пользователь</h2>
		<p>На данной странице отбражены основные данные сотрудника.</p>
		<p>Дополнительно на этой странице есть возможность сохранить
			записи учета в файл .xls.</p>
		<div class="divWithBorder" style="max-width: 600px">
			<h3>Данные сотрудника</h3>
			<table class="mainTable">
				<tbody>
					<tr>
						<td>Имя пользователя</td>
						<td>sidorov</td>
					</tr>
					<tr>
						<td>Имя</td>
						<td>Федор Петрович</td>
					</tr>
					<tr>
						<td>Фамилия</td>
						<td>Сидоров</td>
					</tr>
					<tr>
						<td>Адрес почты</td>
						<td>sidorov@west-e.ru</td>
					</tr>
					<tr>
						<td>Должность</td>
						<td>Инженер-программист</td>
					</tr>
					<tr>
						<td>Отдел</td>
						<td>ОПиК</td>
					</tr>
				</tbody>
			</table>
		</div>

		<div id="saveContainer">
			<form action="#" method="GET">
				<div id="saveInnerContainer">
					<label>Отчет: </label> <input type="text" class="datepicker-here"
						data-position="bottom left" data-range="true"
						data-multiple-dates-separator=" - " name="period"
						required="required" /> <input type="submit" value="сохранить" />
				</div>
			</form>
		</div>
		<p>Для сохранения записей учета в файл .xls необходимо в поле
			"Отчет" ввести диапазон дат в формате "DD-MM-YYYY - DD-MM-YYYY" (без
			кавычек), пример: "13.01.2016 - 25.02.2016". Или выбрать даты на
			календаре. Календарь автоматически отобразится при щелчке левой
			кнопки мыши в поле "Отчет". После введения даты необходимо нажать
			кнопку "сохранить" и откроется стандартное окно сохранения файла.</p>
	</article>

	<article id="update" class="update-article">
		<h2>Обновления</h2>
		<p>На данной странице отображены обновления в системе учета
			рабочего времени.</p>
		<h3>20.02.2018</h3>
		<p>Добавлена <a href="#sub-section-9">общая статистика</a>.
		<p>Добавлена возможность заполнения формы новой записи учета рабочего времени
		по щелчку левой кнопку мыши на старых записях.</p>
		<h3>09.10.2017</h3>
		<p>
			Добавлен <a href="#sub-section-10">календарь отпусков</a>.
		</p>
		<p>Добавлена возможность просмотра статистики по проектам для
			статистов.</p>
		<p>Добавлено автоматическое обновление страницы в браузере каждый
			час.</p>
		<p>Изменена функция кнопки сохранить на странице учета времени,
			теперь сохраняется текущий квартал.</p>
		<p>Добавлен расчет итогов за квартал при экспорте файла.</p>
		<h3>20.06.2017</h3>
		<p>Добавлен рабочий календарь.</p>
		<p>Добавлен расчет переработок.</p>
		<h3>05.04.2017</h3>
		<p>Добавлено поле номер договора для проектов.</p>
		<p>Добавлена возможность изменять статус проектов и задач.</p>
		<h3>29.03.2017</h3>
		<p>Добавлена сортировка в выпадающих меню.</p>
		<p>Добавлена проверка на ввод корректной даты на странице учета.</p>
		<p>
			Добавлена возможность <a href="#sub-section-4">изменить</a> атрибуты
			ранее созданного проекта.
		</p>
		<p>
			Добавлена возможность <a href="#sub-section-6">изменить</a> атрибуты
			ранее созданной задачи.
		</p>
		<p>
			Добавлена возможность <a href="#employees">просмотра записей
				учета</a> времени подчиненнных сотрудников.
		</p>
		<p>
			Добавлен тип <a href="#stat">статистики</a> по времени для проектов и
			сотрудников.
		</p>
		<p>Исправлена ошибка расчета переработок по выходным дням при
			экспорте в файл .xls.</p>
	</article>

</div>


<script>
	var ctx1 = document.getElementById("chartByProjects").getContext("2d");
	var ctx2 = document.getElementById("barChartByProjects").getContext("2d");
	var ctx3 = document.getElementById("chartBy").getContext("2d");
	var ctx4 = document.getElementById("barChartBy").getContext("2d");

	$(document).ready(function() {
		getStatistic();
		$("#buttonRefresh").click(function() {
			getStatistic();
		});
		getStatisticPrj();
		$("#buttonRefreshPrj").click(function() {
			getStatisticPrj();
		});
	});

	function getStatistic() {

		var labels = [ "САУ ДГУ", "АСКРО", "СКУ ЭЧ" ];
		var counts = [ 100, 25, 75 ];

		dataBy = {
			labels : labels,
			datasets : [ {
				data : counts,
				backgroundColor : [ "#FF6384", "#36A2EB", "#FFCE56" ],
				hoverBackgroundColor : [ "#FF6384", "#36A2EB", "#FFCE56" ]
			} ]
		};

		var myPieChart = new Chart(ctx1, {
			type : 'pie',
			data : dataBy,
			options : {
				common : {
					responsive : false
				}
			}
		});

		var myBarChart = new Chart(ctx2, {
			type : 'bar',
			data : dataBy,
			options : {
				scales : {
					yAxes : [ {
						ticks : {
							beginAtZero : true
						}
					} ]
				},
				legend : {
					display : false
				}
			}
		});
	};

	function getStatisticPrj() {

		var labels = [ "Сидоров Федор Петрович", "Соболев Николай Альбертович" ];
		var counts = [ 50, 25 ];

		dataBy = {
			labels : labels,
			datasets : [ {
				data : counts,
				backgroundColor : [ "#FF6384", "#36A2EB", "#FFCE56" ],
				hoverBackgroundColor : [ "#FF6384", "#36A2EB", "#FFCE56" ]
			} ]
		};

		var myPieChart = new Chart(ctx3, {
			type : 'pie',
			data : dataBy,
			options : {
				common : {
					responsive : false
				}
			}
		});

		var myBarChart = new Chart(ctx4, {
			type : 'bar',
			data : dataBy,
			options : {
				scales : {
					yAxes : [ {
						ticks : {
							beginAtZero : true
						}
					} ]
				},
				legend : {
					display : false
				}
			}
		});
	};
</script>

<script type="text/javascript">
	$(document).ready(function() {
		$('#selectProject').SumoSelect({
			placeholder : 'Выберите проект из списка',
			search : true,
			searchText : 'Поиск по названию проекта'
		});
		$('#selectTask').SumoSelect({
			placeholder : 'Выберите задачу из списка',
			search : true,
			searchText : 'Поиск по названию задачи'
		});
		$('#selectManagers').SumoSelect({
			placeholder : 'Выберите из списка',
			search : true,
			searchText : 'Введите имя или фамилию...'
		});
		$('#selectManagersUpdate').SumoSelect({
			placeholder : 'Выберите из списка',
			search : true,
			searchText : 'Введите имя или фамилию...'
		});
		$('#selectStatus').SumoSelect({
			placeholder : 'Выберите из списка',
			search : true,
			searchText : 'Введите статус...'
		});
		$('#selectStatusUpdate').SumoSelect({
			placeholder : 'Выберите из списка',
			search : true,
			searchText : 'Введите статус...'
		});

		$('#selectStatusTask').SumoSelect({
			placeholder : 'Выберите из списка',
			search : true,
			searchText : 'Введите статус...'
		});

		$('#selectStatusTaskUpdate').SumoSelect({
			placeholder : 'Выберите из списка',
			search : true,
			searchText : 'Введите статус...'
		});
	});
	$(document).ready(function() {
		//В зависимости от выбора checkbox-а (Все проекты/Все задачи)
		//скрываем или отображаем возможные варианты
		$("#typeAllItems").click(function() {
			if ($("#typeAllItems").prop("checked")) {
				$("#items").hide();
			} else {
				//Сброс вариантов
				$("#selectItems").empty();
				$("#selectItems .optWrapper .options").empty();
				// Загружаем возможные варианты (проекты или задачи)
				var items;
				if ($("#statType").val() == 1) {
					items = [ "САУ ДГУ", "АСКРО", "СКУ ЭЧ" ];
				} else {
					items = [ "ПО", "РЭ", "ПНР" ];
				}

				items.forEach(function(entry) {
					$("#selectItems")[0].sumo.add(entry);
				});

				$("#selectItems").SumoSelect({
					placeholder : 'Выберите из списка',
					search : true,
					searchText : 'Введите имя проекта...'
				});
				$("#items").slideDown("slow");
			}
		});
		// Изменяем текст checkbox-а в зависимости от выбора типа фильтра
		$("#statType").change(function() {
			$("#typeAllItems").prop("checked", true);
			$("#items").hide();
			if ($("#statType").val() == 1) {
				$("#typeAllItemsText").text("Все задачи");
			} else {
				$("#typeAllItemsText").text("Все проекты");
			}
		});
		$("#statTypePrj").change(function() {
			$("#typeAllItemsPrj").prop("checked", true);
			$("#itemsPrj").hide();
			if ($("#statTypePrj").val() == 1) {
				$("#typeAllItemsPrjText").text("Все задачи");
			} else {
				$("#typeAllItemsPrjText").text("Все сотрудники");
			}
		});
	});
</script>
