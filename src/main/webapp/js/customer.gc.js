$(function () {
    loadECharts();
});
function loadECharts() {
    $.ajax({
        type:'post',
        url:ctx+'/customer/queryCustomerGC',
        dataType:"json",
        success:function (data) {
            if(data.code==200){
                var x=data.levels;
                var y=data.counts;

                // 基于准备好的dom，初始化echarts实例
                var myChart = echarts.init(document.getElementById('main'));

                // 指定图表的配置项和数据
                var option = {
                    title: {
                        text: '客户构成分析'
                    },
                    tooltip: {},
                    legend: {
                        data:['人数']
                    },
                    xAxis: {
                        data: x
                    },
                    yAxis: {},
                    series: [{
                        name: '人数',
                        type: 'bar',
                        data: y
                    }]
                };

                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(option);
            }
        }
    });

}