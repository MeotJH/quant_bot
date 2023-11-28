const accessTokenName = "Authorization";
const refreshTokenName = "RefreshToken";

//fetch함수 wraping 함수
const quantFetch = function (url,data = {
    /**
     * 두번째 파람 값 없을때 default value
     */
    method: "GET",
    headers: {
        "Content-Type": "application/json",
    }
}) {

    if(!data.headers){
        data.headers = {"Content-Type": "application/json"}
    }

    return new Promise(function(resolve, reject) {
        try {
            fetch(url,{
                method: data.method,
                headers: data.headers,
                body: JSON.stringify(data.body)
            })
                .then(async response => {
                    if (!response.ok) {
                        //TODO 로그인 실패 프론트 만들기 JSON.parse(resultData);
                        const body = JSON.parse(await response.json());
                        resolve(body)
                        throw new Error( `${response.url}  ${response.status}  ${body.message}` );
                    }
                    return response.json();
                })
                .then(data => {
                    if(data.error){
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
 * time당 progress에 25씩 더해주는 함수
 * @param originVariation
 * @param time
 * @returns {Promise<unknown>}
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
/**
 * array를 받아서 chunk 만큼 자른 배열을 다시 만들어 리턴해주는 함수
 * @param array
 * @param chunk
 * @returns {*[]}
 */
const splitIntoChunk = (array, chunk) => {
    const copyArray = Object.assign([],array);
    const result = [];

    while(copyArray.length > 0) {
        let tempArray;
        tempArray = copyArray.splice(0, chunk);
        result.push(tempArray);
    }

    return result;
}

const changeLoginTagBaseOnStatus = () => {
    const tag = document.getElementById("login-tag");
    const localStorage = window.localStorage;
    const token = getCookie("Authorization");
    let loginStatus = "login";
    tag.getElementsByClassName("nav-link")[0].setAttribute("href","/view/"+loginStatus);
    if(token){
        loginStatus = "logout";
        tag.setAttribute("onclick","logout()")
        tag.getElementsByClassName("nav-link")[0].setAttribute("href","#");
    }
    tag.getElementsByClassName("nav-link")[0].textContent = loginStatus;
}

const logout = () =>{

    deleteCookie(accessTokenName);
    deleteCookie(refreshTokenName);
    changeLoginTagBaseOnStatus();

}

const isAuth = () => {
    const token = getCookie(accessTokenName);
    return token ?  true : false;
}

const goAuthPage = (url) =>{
    const token = getCookie(accessTokenName);
    if(token){
        location.href = url;
    }else{
        alert("서비스를 이용하기 위해 로그인을 해주세요");
        location.href = "/view/login";
    }
}

function getCookie(cookieName) {
    const name = cookieName + "=";
    const decodedCookie = decodeURIComponent(document.cookie);
    const cookieArray = decodedCookie.split(';');

    for (let i = 0; i < cookieArray.length; i++) {
        let cookie = cookieArray[i].trim();
        if (cookie.indexOf(name) === 0) {
            return cookie.substring(name.length, cookie.length);
        }
    }

    return null;
}

function deleteCookie(cookieName) {
    document.cookie = cookieName + "=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
}