const formatDishes = (dishes)=>{
    return dishes.map(dish => `${dish.name} \t x${dish.count}`).join('\n');
}

export function pushMessage(dishesList, totalPrice) {
    const key = 'Qndcjv9tmLsoB3cYuSPaPi'; // Bark密钥

    fetch(`https://api.day.app/${key}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            icon: 'https://pic.imgdb.cn/item/671a3c7dd29ded1a8c1047b3.png', // 推送图标
            title: '口袋点餐-新订单', // 推送标题
            body: formatDishes(dishesList) + `\n\n\t\t\t\t\t\t\t总计 : ¥ ${totalPrice}`, // 推送内容
            group: '口袋点餐', // 分组
            url: `https://pco.xyquan.top/order`,
        })
    })
        .then((res) => {
            if (res.ok) {
                console.log('sentry success');
            } else {
                console.error('sentry failed:', res.statusText);
            }
        })
        .catch((err) => {
            console.error('sentry error:', err);
        });
}
