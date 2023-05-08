'use strict';

$(document).ready(function () {
    const tbody = document.getElementById("productsTable").tBodies[0];
    let productsId = JSON.parse(document.getElementById("products").value).map(product => product.id);
    const idToRowMap = createIdToRowMap(productsId);

    function createIdToRowMap(idList) {
        let map = new Map();
        for (let i = 0; i < idList.length; i++) {
            map.set(idList[i], tbody.rows[i]);
        }
        return map;
    }

    function sortProducts(event) {
        const xmlHttpRequest = new XMLHttpRequest();
        xmlHttpRequest.onreadystatechange = function () {
            if (this.readyState === 4 && this.status === 200) {
                productsId = JSON.parse(this.responseText);
                sortTable(productsId);
            }
        };
        xmlHttpRequest.open("GET", event.target.href, true);
        xmlHttpRequest.send();
        event.preventDefault();
    }

    function sortTable(idList) {
        for (let i = 0; i < idList.length; i++) {
            if (idToRowMap.has(idList[i])) {
                tbody.insertBefore(idToRowMap.get(idList[i]), tbody.rows[i]);
            }
        }
    }

    $('#productsTable thead tr td a').on('click', sortProducts);
});
