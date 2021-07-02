var ctx = document.getElementById('myChart').getContext('2d');
var myChart = new Chart(ctx, {
    type:'bar',
    data: {
        labels: ['red','blue'],
        datasets: [{
            label: '# of votes',
            data: [12, 13]
        }]
    },
    options:{
        scales: {
            y: {
                beginAtZero: true
            }
        }
    }
});
