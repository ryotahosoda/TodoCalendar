
document.addEventListener('DOMContentLoaded', function() {
    var calendarEl = document.getElementById('calendar');

    var calendar = new FullCalendar.Calendar(calendarEl, {

        plugins: [ 'interaction', 'dayGrid', 'list', 'googleCalendar' ],

        header: {
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth,listYear'
        },

        displayEventTime: false, // don't show the time column in list view

        events: [
            { // this object will be "parsed" into an Event Object
                title: 'The Title', // a property!
                start: '2019-09-26', // a property!
                end: '2019-09-27' // a property! ** see important note below about 'end' **
            }
        ],


        eventClick: function(arg) {
            // opens events in a popup window
            window.open(arg.event.url, 'google-calendar-event', 'width=700,height=600');

            arg.jsEvent.preventDefault() // don't navigate in main tab
        },

        loading: function(bool) {
            document.getElementById('loading').style.display =
                bool ? 'block' : 'none';
        }

    });

    calendar.render();
});