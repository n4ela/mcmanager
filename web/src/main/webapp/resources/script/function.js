function showDialogEditor(edit) {
    if (document.getElementById("filmTable").className.indexOf('hide') == -1) {
        dialogFilm(edit);
    } else {
        dialogGroup(edit);
    }
}

function dialogGroup(edit) {
    var title = edit ? "Редактирование группы" : "Добавление группы";
    $("#dialogGroupEditor").dialog({
        title: title,
        modal: true,
        width: 550,
        height: 350,
        buttons: {
            "Сохранить" : function() {
                if (edit)
                    document.getElementById("updateGroup").onclick();
                else
                    document.getElementById("saveGroup").onclick();
            },
            "Отменить" : function() {
                $(this).dialog("close");
            }
        }
    });
    $("#dialogGroupEditor").parent().appendTo($("FORM"));
}

function dialogFilm(edit) {
    var title = edit ? "Редактирование раздачи" : "Добавление раздачи";
    $("#dialogFilmEditor").dialog({
        title: title,
        modal: true,
        width: 550,
        height: 500,
        buttons: {
            "Torrent" : function() {
                document.getElementById("testTorrent").onclick();
            },
            "Пример сообщения" : function() {
                document.getElementById("testMessage").onclick();
            },
            "Сохранить" : function() {
                if (edit)
                    document.getElementById("updateFilm").onclick();
                else
                    document.getElementById("saveFilm").onclick();
            },
            "Отменить" : function() {
                $(this).dialog("close");
            }
        }
    });
    $("#dialogFilmEditor").parent().appendTo($("FORM"));
}

//function changeTable() {
//	$('#filmTable').toggle();
//    $('#groupTable').toggle();
//    showControlPanel();
//}

//function showControlPanel() {
//	if (isFilmTableActive()) {
//	    document.getElementById("renderNavigationPanel").onclick();
//	} else {
//		$('#labelGroup').hide();
//	    $('#selectGroup').hide();
//	    $('#inputSearch').hide();
//	}
//}


//function isFilmTableActive() {
//	return document.getElementById("filmTable").style.display != "none" ? true : false;
//}

function closeGroupDialog(data) {
    if (data.status == "success" &&
            (document.getElementById("messageGroupId") == null || document.getElementById("messageGroupId").textContent == "") &&
            (document.getElementById("messageGroupName") == null || document.getElementById("messageGroupName").textContent == "") &&
            (document.getElementById("messageGroupTorrentFolder") == null || document.getElementById("messageGroupTorrentFolder").textContent == "") &&
            (document.getElementById("messageGroupMediaFolder") == null || document.getElementById("messageGroupMediaFolder").textContent == "") &&
            (document.getElementById("messageGroupDownloadFolder") == null || document.getElementById("messageGroupDownloadFolder").textContent == "") &&
            (document.getElementById("messageGroupEmailMessage") == null || document.getElementById("messageGroupEmailMessage").textContent == "") &&
            (document.getElementById("messageGroupEmailRegexp") == null || document.getElementById("messageGroupEmailRegexp").textContent == "")
            )
    {
        $("#dialogGroupEditor").dialog("close");
    }
}

function closeFilmDialog(data) {
    if (data.status == "success" &&
            (document.getElementById("messageFilmId") == null || document.getElementById("messageFilmId").textContent == "") &&
            (document.getElementById("messageFilmTitle") == null || document.getElementById("messageFilmTitle").textContent == "") &&
            (document.getElementById("messageFilmLinkRutracker") == null || document.getElementById("messageFilmLinkRutracker").textContent == "") &&
            (document.getElementById("messageFilmLinkKinoposk") == null || document.getElementById("messageFilmLinkKinoposk").textContent == "") &&
            (document.getElementById("messageFilmGroup") == null || document.getElementById("messageFilmGroup").textContent == "") &&
            (document.getElementById("messageFilmType") == null || document.getElementById("messageFilmType").textContent == "") &&
            (document.getElementById("messageFilmSeasonNumber") == null || document.getElementById("messageFilmSeasonNumber").textContent == "") &&
            (document.getElementById("messageFilmRegexpSerialNumber") == null || document.getElementById("messageFilmRegexpSerialNumber").textContent == "") &&
            (document.getElementById("messageFilmStatus") == null || document.getElementById("messageFilmStatus").textContent == "") &&
            (document.getElementById("messageFilmMailMessage") == null || document.getElementById("messageFilmMailMessage").textContent == "") &&
            (document.getElementById("messageFilmMailRegexp") == null || document.getElementById("messageFilmMailRegexp").textContent == "") &&
            (document.getElementById("messageFilmTorrent") == null || document.getElementById("messageFilmTorrent").textContent == "")
            )
    {
        $("#dialogFilmEditor").dialog("close");
    }
}