/**
 * Global function to display the status message.
 * This message has defined colors such as:
 * Status:
 * 1: warning (yellow)
 * 2: success (green)
 * 4, 5: error: (red)
 * else: info: (blue)
 *
 * @param status the status a value between 1,2,4,5 or other
 * @param teaser a teaser (one word or short description), will be displayed in bold font
 * @param message the real message
 */
function change_status(status, teaser, message) {
    status_class = "alert-"
    switch (status){
        case 1:
            status_class += "warning"
            break;
        case 2:
            status_class += "success"
            break;
        case 4:
        case 5:
            status_class += "danger"
            break;
        default:
            status_class += "info"
            break;
    }

    statusObj = $('#status')
    statusObj.addClass(status_class);
    statusObj.html('<b>' + teaser + ':</b> ' + message);
    statusObj.fadeIn('slow');
    setTimeout(function() {
        statusObj.fadeOut(500, function() {
            statusObj.removeClass(status_class);
        });
    }, 4000);
}

/*
 * This function get all date from timestamp
 */
function dataFromTimestamp(timestamp){
    var d = new Date(timestamp);

    // Time
    var h = addZero(d.getHours());              //hours
    var m = addZero(d.getMinutes());            //minutes
    var s = addZero(d.getSeconds());            //seconds

    // Date
    var da = d.getDate();                       //day
    var mon = d.getMonth() + 1;                 //month
    var yr = d.getFullYear();                   //year
    var dw = d.getDay();                        //day in week

    // Readable feilds
    months = ["jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec"];
    var monName = months[d.getMonth()];         //month Name
    var time = h + ":" + m + ":" + s;           //full time show
    var thisDay = da + "/" + mon + "/" + yr;    //full date show

    var dateTime = {
        seconds : s,
        minutes : m,
        hours : h,
        dayInMonth : da,
        month : mon,
        year : yr,
        dayInTheWeek : dw,
        monthName : monName,
        fullTime : time,
        fullDate : thisDay
    };
    return dateTime;

    function addZero(i) {
        if (i < 10) {
            i = "0" + i;
        }
        return i;
    }
}

/**
 * Calls the Configuration API to retrieve the config object.
 *
 * @param onSuccess the function that should be executed on success.
 */
function getHeartBeats(onSuccess) {
    console.log("sending: GET to /heartbeat");
    // $('#loading-animation').fadeIn();
    $.ajax({
        url: 'heartbeat',
        type: 'GET',
        success: function(res) {
            console.log(res)
            onSuccess(res);
        },
        error: function (e) {
            console.log(e);
            change_status(4, e.responseJSON.error, e.responseJSON.message);
        }
    });
}

$( document ).ready(function() {
    getHeartBeats(function(res) {
        res.forEach(function(elem) {
            console.log(elem)
            var template = Handlebars.compile($('#table-content').html());
            if(elem.lifeState == "ONLINE") {
                elem.lifeStateLabel = "success";
            } else {
                elem.lifeStateLabel = "danger";
            }

            elem.printableDate = dataFromTimestamp(elem.lastSeen);
            $('#target tr:last').after(template(elem)).slideDown();
        })
    });
});

