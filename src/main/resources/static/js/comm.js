//fetch함수 wraping 함수
const quantFetch = function (url) {
    return new Promise(function(resolve, reject) {
        try {
            fetch(url)
                .then(response => {
                    if (!response.ok) {
                        return response.json();
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(data => {
                    if(data.error){
                        console.error(data)
                        alert(data.message)
                        throw new Error('Network response was not ok');
                    }
                    resolve(data);
                })
                .catch(error => {
                    console.error('Request failed:', error);
                });
        } catch (error) {
            console.error('An error occurred:', error);
        }

    });
};

//차트js comm으로 만들 로직
const callChart = function (id,stockData){
    new Chart(document.getElementById(id), {
        type: 'line',
        data: {
            labels: stockData.baseDt.reverse(),
            datasets: [{
                label: '추세이동평균선',
                data: stockData.trendFollowPrices.reverse(),
                order: 1
            },{
                label: '금일 종가',
                data: stockData.closePrice.reverse(),
                type: 'line',
                order: 1
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: false
                }
            }
        }
    });
}

/**
 * 0.5초당 진행률25 씩 오르는 함수
 * @param originVariation
 * @returns {number}
 */
const loadingProgressOverTime = function(originVariation,time){
    return new Promise((resolve) => {
        setTimeout(() =>{
            let progress = originVariation;
            if( 0 <= progress && progress < 100 ){
                progress += 25;
            }else{
                progress = 100;
            }
            resolve(progress);
        }, time)
    });


}