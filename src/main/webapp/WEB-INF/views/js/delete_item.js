function myFunction(link) {
    const r = confirm("Are you sure?");
    if (r === true) {
        window.location.href = link;
    }
}