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
        input.textContent = "";
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