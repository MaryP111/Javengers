function getEntries(productId)
{
    localStorage.setItem("productId", productId);
    window.location.href = "/entry/list";
}
