const accessTokenName = "Authorization";
const refreshTokenName = "RefreshToken";

const isLogin = () =>{
    return  getCookie(accessTokenName) ? true : false
}

//fetch함수 wraping 함수
const quantFetch = function (url,data = {
    /**
     * 두번째 파람 값 없을때 default value
     */

    //response.setHeader("Access-Control-Allow-Origin", "*");


    method: "GET",
    headers: {
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "*"
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
                        if(response.status == 401 || response.status == 403){
                            quantAlert('권한이 없습니다. 로그인해주세요.', 'danger');
                            return response.json();
                        }
                        if(response instanceof Object){
                            return response.json();
                        }else{
                            //TODO 로그인 실패 프론트 만들기 JSON.parse(resultData);
                            const body = JSON.parse(await response.json());
                            resolve(body)
                            throw new Error( `${response.url}  ${response.status}  ${body.message}` );
                        }
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
    return new Chart(document.getElementById(id), {
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

const isEmptyObject = (param) => {
    return Object.keys(param).length === 0 && param.constructor === Object;
}

const alertPlaceholder = document.getElementById('liveAlertPlaceholder');
const quantAlert = (message, type) => {
    if(!type){
        type = 'success';
    }
    const wrapper = document.createElement('div')
    wrapper.innerHTML = [
        `<div class="alert alert-${type} alert-dismissible mx-2 my-2 show fade " role="alert" >`,
        `   <div>${message}</div>`,
        '   <button type="button" class="btn-close custom-alert-btn" data-bs-dismiss="alert" aria-label="Close"></button>',
        '</div>'
    ].join('');
    alertPlaceholder.append(wrapper);
}

const generateUniqueKey = () => {
    const timestamp = new Date().getTime();
    const random = Math.floor(Math.random() * 1000000);
    return `${timestamp}${random}`;
}

const quantToast = (toastObj) =>{
    const toastLiveContainer = document.getElementById('toastContainer');
    const uniqueKey = 'key' + generateUniqueKey();
    const {body,title,time} = toastObj;


    if(!body || !title || !time ){
        throw new Error("quantToast값 비었음 {},{},{}",body,title,time)
    }

    const wrapper = document.createElement('div')
    wrapper.innerHTML =
        `
        <div
        id="${uniqueKey}"
        class="toast m-2"
        role="alert"
        aria-live="assertive"
        aria-atomic="true"
        data-bs-autohide="false"
        >
            <div class="toast-header">
                <strong class="me-auto">${title}</strong>
                <small>${time} mins ago</small>
                <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
            <div class="toast-body">
                ${body}
            </div>
        </div>`;

    toastLiveContainer.append(wrapper);
    const element = toastLiveContainer.querySelector(`#${uniqueKey}`);
    const toastBootstrap = bootstrap.Toast.getOrCreateInstance(element)
    toastBootstrap.show();
}

const advertisementToast = () =>{
    const toastLiveContainer = document.getElementById('toastContainer');
    const uniqueKey = 'key' + generateUniqueKey();

    const wrapper = document.createElement('div')
    wrapper.innerHTML =
        `
        <div
        id="${uniqueKey}"
        class="toast m-2"
        role="alert"
        aria-live="assertive"
        aria-atomic="true"
        data-bs-autohide="false"
        >
            <div class="toast-header">
                <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
            <div class="toast-body d-flex justify-content-center">
                <a href="https://link.coupang.com/a/bs7AeG" target="_blank" referrerpolicy="unsafe-url"><img src="https://ads-partners.coupang.com/banners/699574?subId=&traceId=V0-301-879dd1202e5c73b2-I699574&w=200&h=200" alt=""></a>
            </div>
        </div>`;

    toastLiveContainer.append(wrapper);
    const element = toastLiveContainer.querySelector(`#${uniqueKey}`);
    const toastBootstrap = bootstrap.Toast.getOrCreateInstance(element)
    toastBootstrap.show();
}


const warnInvest = () =>{
    quantAlert('[주의]\n' +
        '\n' +
        '퀀투봇(이하 사이트)에서 제공되는 정보는 참고사항일 뿐이고 투자에 대한 모든 결정은 투자자의 몫입니다.\n' +
        '사이트에서 검색하는 퀀트종목 가격 등락에 대해서는 정보전달일 뿐 개발자가 책임지지 않습니다.\n' +
        '\n' +
        '사이트에서는 주식 리딩방 가입을 권유하지 않습니다.\n' +
        '만일 이러한 연락을 받으셨다면 모두 사칭이니 피해입지 않도록 주의하시길 바랍니다.\n' +
        '\n' +
        '사이트는 언제나 유저들의 평가를 환영합니다. 하지만 근거없는 비난과 비방으로\n' +
        '개발자의 명예를 훼손할 경우 부득이 법적 조치를 취할 수 있습니다. ', 'danger');
}





