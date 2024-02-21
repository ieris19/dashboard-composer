function addSearchBarJS() {
    let btn = document.getElementById("search-btn");
    let input = document.getElementById("search-bar");
    googleSearchListener(input, btn);
    enterKeyListener(input, btn);
    input.focus();
}

function googleSearchListener(input, btn) {
    const googleURL = "https://www.google.com/";
    let finalQuery = googleURL;
    btn.addEventListener("click", function () {
        let query = input.value.trim();
        if (query.length > 0) {
            let sanitizedQuery = query.replace(" ", "+");
            finalQuery = googleURL + "search?q=" + sanitizedQuery;
        }
        window.open(finalQuery);
        input.value = "";
    });
}

function enterKeyListener(field, btn) {
    field.addEventListener("keypress", function (event) {
        if (event.key === "Enter") {
            event.preventDefault();
            btn.click();
        }
    });
}

function randomBackground() {
    const queryParams = "?crop=entropy&fit=crop&h=1080&w=1920"
    let bg = ["https://images.unsplash.com/photo-1503206557829-9a9979ad1227"
        ,"https://images.unsplash.com/photo-1516117172878-fd2c41f4a759"
        ,"https://images.unsplash.com/photo-1533396371595-d46b0aa39bd2"
        ,"https://images.unsplash.com/photo-1517328894681-0f5dfabd463c"
        ,"https://images.unsplash.com/photo-1649605958244-73d7d5747bcd"
    ];
    let randomBgClass = document.getElementsByClassName("random-bg");
    for (let i = 0; i < randomBgClass.length; i++) {
        let randomIndex = Math.floor(Math.random() * bg.length);
        let finalURL = bg[randomIndex] + queryParams;
        randomBgClass[i].style.backgroundImage = "url(" + finalURL + ")";
    }
}