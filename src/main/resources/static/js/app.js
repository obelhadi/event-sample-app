function extractDate(date) {
    if (!date)
        return "";
    return moment(date).utc().format('DD/MM/YYYY HH:mm');
}

function getDate(event) {
    var res = '<span>';
    if (!!event.beginDate) {
        var beginDate = new Date(event.beginDate);
        res = extractDate(beginDate);
    }
    if (!!event.endDate) {
        var endDate = new Date(event.endDate);
        res += '  to  ' + extractDate(endDate);
    }
    res += '</span>';
    return res;
}

function showEvents(data) {
    if (!!data && data.length > 0) {
        data.forEach(function (event) {
            $('#eventsTable tbody').append(
                '<tr>'
                + '<td class="col-lg-2">' + event.name + '</td>'
                + '<td class="col-lg-4">' + getDate(event) + '</td>'
                + '<td class="col-lg-2">' + event.category + '</td>'
                + '<td class="col-lg-2">' + event.city + '</td>'
                + '<td class="col-lg-1">' + event.zipCode + '</td>'
                + '<td class="1"><div class="row">'
                + '<div class="col-lg-2"><a id="showDetails' + event.id + '"'
                + ' href="pages/details.html?id=' + event.id + '"><i title="Show details" class="glyphicon glyphicon-file"></i></a> </div>'
                + '<div class="col-lg-2"><a id="updateEvent' + event.id + '"'
                + ' href="pages/save.html?actionType=update&id=' + event.id + '"><i title="Edit" class="glyphicon glyphicon-edit"></i></a> </div>'
                + '<div class="col-lg-2"><a class ="deleteEvent"  id="deleteEvent' + event.id + '"' +
                ' onclick="deleteEvent(\'' + event.id + '\')" ' +
                ' href="#"><i title="Remove" class="glyphicon glyphicon-trash"></i></a> </div>'
                + '</td></div>'
                + '</tr>'
            );

        });
    } else {
        showEmptyEventsMsg();
    }
}

function deleteEvent(id) {
    if (window.confirm("Are you sure you wanna delete this event ?")) {
        $.ajax({
            url: "/rest/event/" + id,
            type: 'DELETE',
            contentType: 'application/json'
        }).then(function () {
                window.location.href = "/index.html"
            },
            function (jqXHR, textStatus, errorThrown) {
                console.log('error occured trying to delete event :', textStatus, errorThrown);
            }
        );
    }
}

function getEventById(id, update) {

    $.ajax({
        url: "/rest/event/" + id,
        type: 'GET',
        contentType: 'application/json'
    }).then(function (event) {
            var formattedBeginDate = moment(event.beginDate).utc().format('DD/MM/YYYY HH:mm');
            var formattedEndDate = moment(event.beginDate).utc().format('DD/MM/YYYY HH:mm');
            if (!!update) {
                $('#eventName').val(event.name);
                $('#eventDescription').val(event.description);
                $('#eventCategory').val(event.category);
                $('#eventBeginDate').val(formattedBeginDate);
                $('#eventEndDate').val(formattedEndDate);
                $('#eventInfoUrl').val(event.infoUrl);
                $('#eventCity').val(event.city);
                $('#eventZipCode').val(event.zipCode);
                $('#eventAddress').val(event.address);
            } else {
                $('#eventName').text(event.name);
                $('#eventDescription').text(event.description);
                $('#eventCategory').text(event.category);
                $('#eventBeginD').text(formattedBeginDate);
                $('#eventEndD').text(formattedEndDate);
                $('#eventInfoUrl').text(event.infoUrl);
                $('#eventInfoUrl').attr("href", event.infoUrl);
                $('#eventCity').text(event.city);
                $('#eventZipCode').text(event.zipCode);
                $('#eventAddress').text(event.address);
            }

        },
        function (jqXHR, textStatus, errorThrown) {
            console.log('error occured trying to delete event :', textStatus, errorThrown);
        }
    );
}


function showEmptyEventsMsg() {
    clearTableBody();
    $('#eventsTable tbody').append('<p>There is no events created</p>');
}

function clearTableBody() {
    $('#eventsTable tbody').empty();
}
function getAllEvents() {
    $.ajax({
        url: "/rest/event"
    }).then(function (data) {
        clearTableBody();
        showEvents(data);
    }, function () {
        showEmptyEventsMsg();
    });
}

function search(keyword) {
    $.ajax({
        url: '/rest/event/search?query=' + keyword
    }).then(function (data) {
        clearTableBody();
        showEvents(data);
    }, function () {
        showEmptyEventsMsg();
    });

}

$(document).ready(function () {


    $('#eventBeginDate').datetimepicker({
        format: 'dd/mm/yyyy hh:ii'
    });


    $('#eventEndDate').datetimepicker({
        format: 'dd/mm/yyyy hh:ii'
    });

    $('#cancelEvent').click(function (e) {
        e.preventDefault();
        window.location.href = "/index.html"
    });

    $('input.searchInput').keyup(function () {
        var keyword = $(this).val();
        if (!!keyword) {
            search(keyword);
        } else {
            getAllEvents();
        }

    });

    getAllEvents();

    $('#saveEvent').click(function (e) {
        e.preventDefault();
        var event = {
            name: $('#eventName').val(),
            description: $('#eventDescription').val(),
            category: $('#eventCategory').val(),
            beginDate: $('#eventBeginDate').data().datetimepicker.date,
            endDate: $('#eventEndDate').data().datetimepicker.date,
            infoUrl: $('#eventInfoUrl').val(),
            city: $('#eventCity').val(),
            zipCode: $('#eventZipCode').val(),
            address: $('#eventAddress').val()
        };
        var url = new URL(window.location.href);
        var actionType = url.searchParams.get("actionType");
        var httpMethod = 'POST';
        if (actionType === 'update') {
            var eventId = url.searchParams.get("id");
            event.id = eventId;
            httpMethod = 'PUT';
        }
        console.log('Trying to save ', event);
        $.ajax({
            url: "/rest/event",
            type: httpMethod,
            contentType: 'application/json',
            data: JSON.stringify(event)
        }).then(function () {
                window.location.href = "/index.html"
            },
            function (jqXHR, textStatus, errorThrown) {
                console.log('error occured trying to save event : ', errorThrown);
            }
        );
    })
});