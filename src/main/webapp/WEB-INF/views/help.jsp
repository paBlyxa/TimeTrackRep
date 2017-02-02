<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<head>
	<script src="<c:url value="/resources/script/Chart.js" />"></script>
	<script src="<c:url value="/resources/script/datepicker.min.js" />"></script>
	<link href="<c:url value="/resources/datepicker.min.css"/>" rel="stylesheet" type="text/css">
	<script src="<c:url value="/resources/script/jquery.sumoselect.min.js" />"></script>
	<link href="<c:url value="/resources/sumoselect.css"/>" rel="stylesheet" type="text/css">
</head>

<div class="menuHelp-wrap" id="menuHelp-wrap">		
	<script>
		$(window).scroll(function(){
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
		</div>		
		<div class="nav--section">
			<h2 class="nav--section-title">
				<a href="#tasks">Задачи</a>
			</h2>
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
					<a href="#sub-section-4">Статистика сотрудника</a>
				</h3>
			</div>	
			<div class="nav--subsection">
				<h3 class="nav--subsection-title">
					<a href="#sub-section-5">Статистика проекта</a>
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
										<a href="#">Новый проект</a>
									</div>
								</li>	
								<li><a class="menuItem" href="#tasks">Задачи</a></li>
								<li><a class="menuItem" href="#employees">Сотрудники</a></li>
								<li><a class="menuItem" href="#stat">Статистика</a></li>
								<li><a class="menuItem" href="#help">Помощь</a></li>
								<li id="currentUser"><a class="menuItem" href="#account">sidorov</a></li>
							</ul>
						</div>
					<p>Главное меню содержит пункты: Учет, Проекты, Задачи, Сотрудники, Статистика, Помощь и "текущий пользователь".</p>
					<p>Пункт меню "Учет" позволяет перейти на страницу, где отображается записи за определенную неделю. Также на странице "Учет"
					производится редактирование сохраненных записей и создание новых.</p>
					<p>Пункт меню "Проекты" позволяет перейти на страницу, где отображается список ваших проектов, т.е. проекты, где вы
					являетесь ведущим сотрудником. Данный пункт меню содержит подпункт "Новый проект", доступный только для сотрудника
					с повышенным уровенм доступа. При наведении мыши на	пункт "Проекты" появится подпункт "Новый проект", который позволяет
					перейти на страницу со списком всех проектов и возможностью создать новый проект или удалить старый.</p>
					<p>Пункт меню "Задачи" позволяет перейти на страницу, где отображается список всех задач, и есть возможность
					создать новую задачу. Данный пункт отображается только у пользователей с повышенным доступом.</p>
					<p>Пункт меню "Сотрдуники" позволяет перейти на страницу, где отображается список ваших непосредственных подчиненных.</p>
					<p>Пункт меню "Статистика" позволяет перейти на страницу, где отображается ваша статистика по проектам и задачам.</p>
					<p>Пункт меню "Помощь" позволяет перейти на текущую странице, где описаны основные моменты работы с сайтом.</p>
					<p>Пункт меню "Текущий пользователь" (на картинке выше "sidorov" - отображается имя вашего логина), позволяет перейти на
					страницу с данными вашего аккаунта.</p>
				</article>
				
				<article id="timesheet" class="help-article">
					<h2>Учет</h2>
					<p>При открытии данной страницы отображается таблица с записями за текущую неделю.</p>
					<p>Таблица содержит следующие столбцы:
					дата, проект, задача, часы, часы, комментарий. В столбце "Дата" отображается дата и день недели соответствующей записи
					в таблице. Записи с одинаковой датой объединяются в столбце "Дата" и втором столбце "Часы" в одну строку.
					Столбец "Проект" содержит название проекта над которым выполнялась работа, соответствующая задаче в столбце
					"Задача". В первом столбце "Часы" отображается время выделенное на данную задачу. Во втором стоблце содержится
					сумма часов всех записей за один день. Столбец "Комментарий" содержит различные комментарии, замечания пользователей.</p>
					<p id="sub-section-1">В конце таблицы отображается общее количество часов, отработанных за текущую неделю.</p>
					<p>Данная страница ялвяется страницей по умолчанию (после ввода логина и пароля).</p>
					<div class="listTimesheet">
						<h3>Недавние записи</h3>
						<div class="example">
							<table class="timesheetTable">
									<thead>
										<tr>
											<th scope="col" class="colDate">Дата</th>
											<th scope="col" class="colProject">Проект</th>
											<th scope="col" class="colTask">Задача</th>
											<th scope="col" class="colCount">Часы</th>
											<th scope="col" class="colHours">Часы</th>
											<th scope="col" class="colComment">Комментарий</th>
										</tr>
									</thead>
									<tbody>
										<tr class="odd">
											<td rowspan="1"> понедельник <br /> 26-12-2016</td>
											<td>САУ ДГУ 1 блок ЛАЭС-2</td>
											<td>Разработка эксплуатационной документации</td>
											<td>8.0</td>
											<td rowspan="1">8.0</td>
											<td>Замечания к ПМ ТЗБиС</td>
											<td id="colLast">
												<input type="submit" value="Удалить" onClick="return confirm('Удалить запись?')" />
											</td>
										</tr>
										<tr class="even">
											<td rowspan="2"> вторник <br /> 27-12-2016</td>
											<td>САУ ДГУ 1 блок ЛАЭС-2</td>
											<td>Разработка эксплуатационной документации</td>
											<td>8.0</td>
											<td rowspan="2">9.0</td>
											<td>Замечания к ПМ САР</td>
											<td id="colLast">
												<input type="submit" value="Удалить" onClick="return confirm('Удалить запись?')" />
											</td>
										</tr>
										<tr class="even">
											<td>САУ ДГУ 1 блок ЛАЭС-2</td>
											<td>ПНР</td>
											<td>1.0</td>
											<td></td>
											<td id="colLast">
												<input type="submit" value="Удалить" onClick="return confirm('Удалить запись?')" />
											</td>
										</tr>
										<tr class="odd">
											<td rowspan="1"> среда <br /> 28-12-2016</td>
											<td>САУ ДГУ 1 блок ЛАЭС-2</td>
											<td>ПНР</td>
											<td>1.0</td>
											<td rowspan="1">1.0</td>
											<td></td>
											<td id="colLast">
												<input type="submit" value="Удалить" onClick="return confirm('Удалить запись?')" />
											</td>
										</tr>
										<tr class="even">
											<td rowspan="2"> четверг <br /> 29-12-2016</td>
											<td>АСКРО СХК</td>
											<td>ПО ПЛК</td>
											<td>2.0	</td>
											<td rowspan="2">3.0</td>
											<td></td>
											<td id="colLast">
												<input type="submit" value="Удалить" onClick="return confirm('Удалить запись?')" />
											</td>
										</tr>
										<tr class="even">
											<td>САУ ДГУ 1 блок ЛАЭС-2</td>
											<td>ПНР</td>
											<td>1.0</td>
											<td></td>
											<td id="colLast">
												<input type="submit" value="Удалить" onClick="return confirm('Удалить запись?')" />
											</td>
										</tr>
										<tr class="odd">
											<td rowspan="1"> пятница <br /> 30-12-2016</td>
											<td>САУ ДГУ 1 блок ЛАЭС-2</td>
											<td>ПО ПЛК</td>
											<td>8.0</td>
											<td rowspan="1">8.0</td>
											<td></td>
											<td id="colLast">
												<input type="submit" value="Удалить" onClick="return confirm('Удалить запись?')" />
											</td>
										</tr>
										<tr class="even">
											<td rowspan="0"> суббота <br /> 31-12-2016</td>
											<td></td>
											<td></td>
											<td></td>
											<td></td>
											<td></td>
										</tr>
										<tr class="odd">
											<td rowspan="0"> воскресенье <br /> 01-01-2017</td>
											<td></td>
											<td></td>
											<td></td>
											<td></td>
											<td></td>
										</tr>
									</tbody>
									<tfoot>
										<tr>
											<td style="text-align: right;" colspan="4">Общее количество часов:</td>
											<td colspan="2">29.0</td>
										</tr>
									</tfoot>
								</table>
							<div id="moreUrl">
								<a href="#">Предыдующая неделя</a>
								<a style="float: right;" href="#">Следующая неделя</a>
							</div>
						</div>
					</div>
					<p>Значение в первом столбце "Часы" доступно для изменения. Необходимо выделить значение, ввести новое и нажать кнопку "Enter"
					на клавиатуре, при успешном изменении значение обновится, и пересчитается значение во втором стоблце "Часы".</p>
					<p>Кнопка <input type="submit" value="Удалить" onClick="return confirm('Удалить запись?')" /> позволяет удалить запись
					в соответствующей строке.</p>
					<p>Для перехода к предыдущей или следующей недели используйте ссылки <a href="#">Предыдующая неделя</a> и <a href="#">Следующая неделя</a> соответственно.</p>
					
					<div  id="sub-section-2">
						<p>Для добавления новой записи необходимо заполнить форму "Новая запись" и нажать кнопку <input type="submit" value="Добавить" class="buttonAdd" />.</p>
						<div class="timesheetForm">
							<h3>Новая запись</h3>
							<form id="timesheetForm" onSubmit="return validate()" action="" method="POST">
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
											<td><input id="datepickerTS" name="dates" data-position="bottom left" data-multiple-dates-separator=", " type="text" class="datepicker-here input" required="required" data-multiple-dates="true" value=""/></td>
											<td><select id="selectProject" name="projectId" class="input" required="required">
													<option value="">--Выберите проект</option>
													<option value="1">САУ ДГУ 1 блок ЛАЭС-2</option><option value="2">АСКРО СХК</option><option value="3">СХМ ВХР ЛАЭС2</option><option value="4">Администрирование</option><option value="5">АПТС-Онега</option><option value="6">Метро</option>
												</select></td>
											<td><select id="selectTask" name="taskId" class="input" required="required">
													<option value="">--Выберите задачу</option>
													<option value="1">Разработка технического задания</option><option value="2">Разработка спецификации</option><option value="3">ПО ПЛК</option><option value="4">ПО ВУ</option><option value="5">Разработка эксплуатационной документации</option><option value="6">Формирование ИД</option><option value="7">Обработка ИД</option><option value="8">ПНР</option><option value="9">ПСИ</option><option value="11">Установка и настройка ПО</option><option value="12">Разработка деталей и блоков</option><option value="14">Разное</option>
												</select></td>
											<td><input id="inputCountTime" name="countTime" type="text" list="listTimes" class="input" required="required" value="0.0"/> <datalist
													id="listTimes">
													<option value="0.5" />
													<option value="1.0" />
													<option value="2.0" />
													<option value="4.0" />
													<option value="8.0" />
												</datalist></td>
											<td><input id="comment" name="comment" type="text" class="input" value=""/></td>
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
						<p>Форма "Новая запись" содержит пять полей: Дата, Проект, Задача, Часы и Комментарий.</p>
						<p>Все поля, кроме поля "Комментарий", обязательно требуется заполнить для сохранения записи.</p>
						<p>Поле "Дата" заполняется в формате DD-MM-YYYY, где DD - день месяца, MM - месяц, YYYY - год. Если необходимо создать несколько одинаковых записей для различных дней,
						необходимо перечеслить даты через запятую, например: "24-01-2016, 25-01-2016" (без кавычек). Проще всего воспользоваться календарем и выбрать необходимые даты.
						Календарь автоматически отображается при щелчке левой кнопки мыши в поле "Дата".</p>
						<p>В поле "Проект" вводится название проекта, над которым производилась работа. Можно выбрать название проекта из выпадающего списка, или начать ввод для
						фильтрации по названию. Если требуемого проекта нет в списке и у вас нет доступа для добавления нового проекта, то вам необходимо обратиться к вашему непосредственному
						руководителю.</p>
						<p>В поле "Задача" выбирается название выполняемой задачи. Также как и название проекта, можно выбрать название задачи из выпадающего списка, или 
						начать ввод для фильтрации по назаванию. Если требуемой задачи нет в списке и у вас нет доступа для добавления новой задачи, то вам необходимо обратиться к вашему
						непосредственному руководителю.</p>
						<p>В поле "Часы" вводится количество выделенных часов для данной задачи.</p>
						<p>Поле "Комментарий" не требуется для обязтаельного заполнения. Может использоваться пользователями в различных целях, например, для уточнения выполненной задачи
						или т.п.</p>
					</div>
				</article>
				
				<article id="projects" class="help-article">
					<h2>Проекты</h2>
					<p>При открытии данной страницы отображается таблица со списком проектов, в которых вы являетесь ведущим сотрудником.</p>
					<p>Дополнительно в таблице отображается статус проекта и все ведущие сотрудники проекта.</p>
					<div class="divWithBorder">
						<h3>Мои проекты</h3>
						<table class="mainTable">
							<thead>
								<tr>
									<th class="colProjectName">Проект</th>
									<th class="colProjectActive">Статус</th>
									<th class="colProjectLeaders">Ведущие сотрудники</th>
									<th class="colProjectComment">Комментарий</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>САУ ДГУ</td>
									<td>Актив</td>
									<td>Сидоров Федор, Петров Иван</td>
									<td></td>
								</tr>
								<tr>
									<td>САУ ВО</td>
									<td>Актив</td>
									<td>Сидоров Федор, Соболев Николай</td>
									<td></td>
								</tr>
							</tbody>
							<tfoot></tfoot>
						</table>
					</div>
					<p> Для перехода на страницу статистики проекта, необходимо щелкнуть левой кнопкой мыши на строке, соответствующего проекта.</p>
				</article>
				
				<article id="sub-section-3" class="help-article">
					<h3>Новый проект</h3>
					<p>У сотрудников с повышенным уровнем доступа есть возможность создавать новый проект. Для этого необходимо перейти на страницу "Новый проект",
					выбрав пункт "Новый проект" в главном меню (см. раздел <a href="#menu">Меню</a>).</p>

					<div class="projectForm">
						<h3>Новый проект</h3>
						<form id="projectForm" action="" method="POST">
							<table class="newRecordTable">
								<thead>
									<tr>
										<th class="colProjectName">Проект</th>
										<th class="colProjectActive">Статус</th>
										<th class="colProjectLeaders">Ведущие сотрудники</th>
										<th class="colProjectComment">Комментарий</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td><input required="required" class="input" type="text" name="name" /></td>
										<td>Актив</td>
										<td><select id="selectManagers" name="projectLeaders" multiple="multiple">
												<option value="1">Сидоров Федор Петрович</option>
												<option value="2">Петров Иван Генадьевич</option>
												<option value="3">Соболев Николай Альбертович</option>
										</select><input type="hidden" name="_projectLeaders" value="1"/></td>
										<td><input id="comment" name="comment" type="text" class="input" value=""/></td>
									</tr>
								</tbody>
								<tfoot></tfoot>
							</table>
							<input type="submit" value="Добавить" class="buttonAdd" />
						</form>
					</div>
					<p>Для создания нового проекта необходимо заполнить форму "Новый проект" и нажать кнопку <input type="submit" value="Добавить" class="buttonAdd" />.</p>
					<p>Форма "Новый проект" содержит четыре поля: Проект, Статус, Ведущие сотрудники, Комментарий.</p>
					<p>Поле "Проект" содержит название проекта и должно быть уникально для всех проектов.</p>
					<p>Поле "Статус" содержит статус проекта. Проект находится в двух стадиях: актив (выполняется) и завершен (работы не ведутся). Завершенные проекты не отображаются
					в списке проектов в форме "Новая запись" на странице "Учет" (значит сотрудники не могут добавлять записей для этого проекта).</p>
					<p>Поле "Ведущие сотрудники" - сотрудники, которые могут просматривать статистику по данному проекту.</p>
					<p>Поле "Комментарий" не обязательно для заполнения. Используется для различных целей, например, указание объекта (станции) или другой важной информации для
					данного проекта.</p>
					<p>На странице "Новый проект" также отображается список всех проектов фирмы с возможностью удаления выбранного проекта. Возможно удалить только проект не
					имеющий ни одной записи в учете. При попытке удалить проект с записями - приложение выдаст ошибку.</p>
				</article>
				
				<article id="tasks" class="help-article">
					<h2>Задачи</h2>
					<p>При открытии данной страницы отображается таблица со списком всех задач. Также на данной странице возможно создание новой задачи или удаление старой.</p>
					<p>Данная страница доступна только для сотрудников с повышенным уровнем доступа.</p>
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
										<td>Актив</td>
										<td><input id="comment" name="comment" type="text" class="input" value=""/></td>
									</tr>
								</tbody>
								<tfoot></tfoot>
							</table>
							<input type="submit" class="buttonAdd" value="Добавить" />
						</form>
					</div>
					<p>Для создания новой задачи необходимо заполнить форму "Новая задача" и нажать кнопку <input type="submit" class="buttonAdd" value="Добавить" />.</p>
					<p>Форма "Новая задача" содержит три поля: Задача, Статус, Комментарий.</p>
					<p>Поле "Задача"" содержит название задачи и должно быть уникально для всех задач.</p>
					<p>Поле "Статус" содержит статус данной задачи. Возможны два статуса: активный, неактивный. Неактивные задачи не отображаются в списке задач
					в форму "Новая запись" на странице "Учет" (значит сотрудники не могут добавлять записей для этой задачи).</p>
					<p>Поле "Комментарий" не обязательно для заполнения. Используется для различных целей.</p>
					<p>На странице "Задачи" также отображается список всех созданных задач с возможность удаления или изменения статуса. Возможно удалить только задачу
					не имеющую ни одной записи в учете. При попытке удалить задачу с записями - приложение выдаст ошибку.</p>
					<div class="divWithBorder">
						<h3>Задачи</h3>
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
									<td>Актив</td>
									<td></td>
									<td id="colLast">
										<form action="#" method="POST">
											<input	type="submit" value="Удалить" onClick="return confirm('Удалить задачу?')" /> 
										</form>
									</td>
								</tr>
								<tr>
									<td>Разработка спецификации</td>
									<td>Актив</td>
									<td></td>
										
									<td id="colLast">
										<form action="#" method="POST">
											<input type="submit" value="Удалить" onClick="return confirm('Удалить задачу?')" />
										</form>
									</td>
								</tr>
								<tr>
									<td>ПО ПЛК</td>
									<td>Актив</td>
									<td></td>
									<td id="colLast">
										<form action="#" method="POST">
											<input type="submit" value="Удалить" onClick="return confirm('Удалить задачу?')" />
										</form>
									</td>
								</tr>
								<tr>
									<td>ПО ВУ</td>
									<td>Актив</td>
									<td></td>
									<td id="colLast">
										<form action="#" method="POST">
											<input type="submit" value="Удалить" onClick="return confirm('Удалить задачу?')" />
										</form>
									</td>
								</tr>
								<tr>
									<td>Разработка эксплуатационной документации</td>
									<td>Актив</td>
									<td></td>
									<td id="colLast">
										<form action="#" method="POST">
											<input type="submit" value="Удалить" onClick="return confirm('Удалить задачу?')" />
										</form>
									</td>
								</tr>
							</tbody>
							<tfoot></tfoot>
						</table>
					</div>
					<p>
				</article>
					
				<article id="employees" class="help-article">
					<h2>Сотрудники</h2>
					<p>На данной странице отображается таблица со списком всех ваших непосредственных подчиненных.</p>
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
								
									<tr class="clickable-row"
										data-url="#">
										<td>sidorov</td>
										<td>Федор Петрович</td>
										<td>Сидоров</td>
										<td>sidorov@west-e.ru</td>
										<td>Инженер-программист</td>
										<td>ОПиК</td>
									</tr>
								
									<tr class="clickable-row"
										data-url="#">
										<td>petrov</td>
										<td>Иван Генадьевич</td>
										<td>Петров</td>
										<td>petrov@west-e.ru</td>
										<td>Инженер-программист 3 кат.</td>
										<td>ОПиК</td>
									</tr>
							</tbody>
						</table>
					</div>
					<p>Для перехода на страницу статистики сотрудника, необходимо щелкнуть левой кнопкой мыши на строке, соответствующего сотрудника.</p>				
				</article>
				
				<article id="stat" class="help-article">
					<h2 id="sub-section-4">Статистика</h2>
					<h3>Статистика сотрудника</h3>
					<p>На данной странице отображается статистика сотрудника за выбранный период времени.</p>
					<p>В левой части расположены параметры статистики, позволяющие отфильтровать или изменить тип статистики.</p>
					<p>Возможно отображение двух типов статистики: по проектам или по задачам.</p>
					<p>Статистика по проектам - отображает все проекты и количество часов,
					затраченные на соответствующий проект. Если нужно отобразить статистику часов по проектам, но по определенным задачам,
					то необходимо в фильтре снять галочку "Все задачи" и выбрать необходимые задачи.</p>
					<p>Статистика по задачам - отображает все задачи и количество часов, затраченные
					на соответствующую задачу. Возможно отфильтровать часы по проектам, для этого необходимо снять галочку "Все проекты"
					и отобрать необходимые.</p>
					<p>Статистика отображается за определенный период времени, который возможно изменить в поле "Период".</p>
					<p>После изменения параметров статистики необходимо обновить данные нажатием кнопки <button id="buttonRefresh">Обновить</button>.</p>
					
					<div>
						<nav class="filter">
							<h3>Параметры статистики</h3>
							<h4>Тип</h4>
							<select id="statType">
								<option value="1">по проектам</option>
								<option value="2">по задачам</option>
							</select>
							<h4>Фильтр</h4>
							<input type="checkbox" name="typeAllItems" value="true" id="typeAllItems" checked="checked">
							<span id="typeAllItemsText">Все задачи</span>
							<div id="items" class="items">
								<select id="selectItems" name="selectItems" multiple="multiple">
								</select>
							</div>
							<hr/>
							
							<h4>Период: </h4>
							<input
										id="statPeriod" class="statPeriod"
										type="text" class="datepicker-here" data-position="bottom left"
										data-range="true" data-multiple-dates-separator=" - "
										name="statPeriod" value="01.01.2017 - 27.01.2017" required="required" />
							<hr/>
							<button id="buttonRefresh">Обновить</button>
						
						</nav>
						
						<article>
						
						<div class="divWithBorder">
							<div>
								<div>
									<h3>Сидоров Федор Петрович</h3>
									<span>
									
									</span>
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
				<article id="sub-section-5" class="help-article"> 
					<h3>Статистика проекта</h3>
					<p>На данной странице отображается статистика проекта за выбранный период времени.</p>
					<p>Данная страница доступна только ведущим проекта или пользователям с повышенным уровнем доступа.</p>
					<p>В левой части расположены параметры статистики, позволяющие отфильтровать или изменить тип статистики.</p>
					<p>Возможно отображение двух типов статистики: по сотрудникам или по задачам.</p>
					<p>Статистика по сотрудникам - отображает всех сотрудников и количество часов,
					затраченное соответствующим сотрудником. Если нужно отобразить статистику часов по сотрудникам, но по определенным задачам,
					то необходимо в фильтре снять галочку "Все задачи" и выбрать необходимые задачи.</p>
					<p>Статистика по задачам - отображает все задачи и количество часов, затраченные
					на соответствующую задачу. Возможно отфильтровать часы по сотрудникам, для этого необходимо снять галочку "Все сотрудники"
					и отобрать необходимых.</p>
					<p>Статистика отображается за определенный период времени, который возможно изменить в поле "Период".</p>
					<p>После изменения параметров статистики необходимо обновить данные нажатием кнопки <button id="buttonRefreshPrj">Обновить</button>.</p>
					<div>
						<nav class="filter">
						<h3>Параметры статистики</h3>
						<h4>Тип</h4>
						<select id="statTypePrj">
							<option value="1">по сотрудникам</option>
							<option value="2">по задачам</option>
						</select>
						<h4>Фильтр</h4>
						<input type="checkbox" name="typeAllItemsPrj" value="true" id="typeAllItemsPrj" checked="checked">
						<span id="typeAllItemsPrjText">Все задачи</span>
						<div id="itemsPrj" class="items">
							<select id="selectItemsPrj" name="selectItemsPrj" multiple="multiple">
							</select>
						</div>
						<hr/>
						
						<h4>Период: </h4>
						<input
									id="statPeriodPrj" class="statPeriod"
									type="text" class="datepicker-here" data-position="bottom left"
									data-range="true" data-multiple-dates-separator=" - "
									name="statPeriod" value="01.01.2017 - 27.01.2017" required="required" />
						<hr/>
						<button id="buttonRefreshPrj">Обновить</button>
					</nav>
					
					<article>
					
					<div class="divWithBorder">
						<div>
							<div>
								<h3>САУ ДГУ</h3>
								<span>
								
								</span>
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
				
				<article id="help" class="help-article">
					<h2>Помощь</h2>
					<p>На данной странице отображено краткое описание системы.</p>
				</article>
				
				<article id="account" class="help-article">
					<h2>Пользователь</h2>
					<p>На данной странице отбражены основные данные сотрудника.</p>
					<p>Дополнительно на этой странице есть возможность сохранить записи учета в файл .xls.</p>
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
					
					<div id="saveContainer" >
							<form action="#" method="GET">
								<div id="saveInnerContainer">
									<label>Отчет: </label>
									<input type="text"
										class="datepicker-here" data-position="bottom left"
										data-range="true" data-multiple-dates-separator=" - "
										name="period" required="required" />
									<input type="submit" value="сохранить" />
								</div>
							</form>
					</div>		
					<p>Для сохранения записей учета в файл .xls необходимо в поле "Отчет" ввести диапазон дат
					в формате "DD-MM-YYYY - DD-MM-YYYY" (без кавычик), пример: "13.01.2016 - 25.02.2016". Или
					выбрать даты на календаре. Календарь автоматически отобразится при щелчке левой кнопки мыши
					в поле "Отчет". После введения даты необходимо нажать кнопку "сохранить" и откроется стандартное
					окно сохранения файла.</p>  
				</article>				
			</div>

		
<script>
var ctx1 = document.getElementById("chartByProjects").getContext("2d");
var ctx2 = document.getElementById("barChartByProjects").getContext("2d");
var ctx3 = document.getElementById("chartBy").getContext("2d");
var ctx4 = document.getElementById("barChartBy").getContext("2d");

$(document).ready(function() {
	getStatistic();
	$("#buttonRefresh").click(function(){
		getStatistic();
	});
	getStatisticPrj();
	$("#buttonRefreshPrj").click(function(){
		getStatisticPrj();
	});
 });

function getStatistic() {
	
	var labels = ["САУ ДГУ", "АСКРО", "СКУ ЭЧ"];
	var counts = [100,25,75];
		
	dataBy = {
		labels: labels,
		datasets: [
		{
			data: counts,
			backgroundColor: [
				"#FF6384",
				"#36A2EB",
				"#FFCE56"
			],
			hoverBackgroundColor: [
				"#FF6384",
				"#36A2EB",
				"#FFCE56"
			]
		}]
	};
				
	var myPieChart = new Chart(ctx1,{
		type: 'pie',
		data: dataBy,
		options: {
			common: {
		   	responsive: false
		    }
		}
	});
				
	var myBarChart = new Chart(ctx2, {
	    type: 'bar',
	    data: dataBy,
	    options: {
		   	scales : {
		  		yAxes: [{
		   			ticks: {
		   				beginAtZero: true
		   			}
		   		}]
		   	},
		   	legend: {
	            display: false
		   	}
		}
	});
};

function getStatisticPrj() {
	
	var labels = ["Сидоров Федор Петрович", "Соболев Николай Альбертович"];
	var counts = [50,25];
		
	dataBy = {
		labels: labels,
		datasets: [
		{
			data: counts,
			backgroundColor: [
				"#FF6384",
				"#36A2EB",
				"#FFCE56"
			],
			hoverBackgroundColor: [
				"#FF6384",
				"#36A2EB",
				"#FFCE56"
			]
		}]
	};
				
	var myPieChart = new Chart(ctx3,{
		type: 'pie',
		data: dataBy,
		options: {
			common: {
		   	responsive: false
		    }
		}
	});
				
	var myBarChart = new Chart(ctx4, {
	    type: 'bar',
	    data: dataBy,
	    options: {
		   	scales : {
		  		yAxes: [{
		   			ticks: {
		   				beginAtZero: true
		   			}
		   		}]
		   	},
		   	legend: {
	            display: false
		   	}
		}
	});
};
</script>

<script type="text/javascript">
$(document).ready(function() {
	$('#selectProject').SumoSelect({
		placeholder: 'Выберите проект из списка',
		search: true,
		searchText: 'Поиск по названию проекта'
		});
});
$(document).ready(function() {
	$('#selectTask').SumoSelect({
		placeholder: 'Выберите задачу из списка',
		search: true,
		searchText: 'Поиск по названию задачи'
		});
});
$(document).ready(function() {
	$('#selectManagers').SumoSelect({
		placeholder: 'Выберите из списка',
		search: true,
		searchText: 'Введите имя или фамилию...'
		});
});
$(document).ready(function() {
	//В зависимости от выбора checkbox-а (Все проекты/Все задачи)
	//скрываем или отображаем возможные варианты
	$("#typeAllItems").click(function() {
		if ($("#typeAllItems").prop("checked")) {
			$("#items").hide();
		}
		else {
			//Сброс вариантов
			$("#selectItems").empty();
			$("#selectItems .optWrapper .options").empty();
			// Загружаем возможные варианты (проекты или задачи)
			var items; 
			if ($("#statType").val() == 1) {
		 		items = ["САУ ДГУ", "АСКРО", "СКУ ЭЧ"];
			}
			else {
				items = ["ПО", "РЭ", "ПНР"];
			}
				
			items.forEach(function(entry) {
				$("#selectItems")[0].sumo.add(entry);
			});
						
			$("#selectItems").SumoSelect({
				placeholder: 'Выберите из списка',
				search: true,
				searchText: 'Введите имя проекта...'
			});
			$("#items").slideDown("slow");
		}
	});
	// Изменяем текст checkbox-а в зависимости от выбора типа фильтра
	$("#statType").change(function() {
		$("#typeAllItems").prop( "checked", true );
		$("#items").hide();
		if ($("#statType").val() == 1){
			$("#typeAllItemsText").text("Все задачи");
		}
		else {
			$("#typeAllItemsText").text("Все проекты");
		}
	});
	$("#statTypePrj").change(function() {
		$("#typeAllItemsPrj").prop( "checked", true );
		$("#itemsPrj").hide();
		if ($("#statTypePrj").val() == 1){
			$("#typeAllItemsPrjText").text("Все задачи");
		}
		else {
			$("#typeAllItemsPrjText").text("Все сотрудники");
		}
	});	
});
</script>
		