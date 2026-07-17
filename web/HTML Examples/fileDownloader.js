
function setProgress(svgSelector, percent) {
    const circle = document.querySelector(svgSelector + ' .progress-ring__progress');
    const circumference = 2 * Math.PI * circle.r.baseVal.value;
    const offset = circumference - (percent / 100) * circumference;
    circle.style.strokeDashoffset = offset;
}
// Вызовите эту функцию с селектором SVG и процентом загрузки
// setProgress('.fileDownloader.progressing .progress-ring', 50);

// change download icon to progress indicator
document.addEventListener('DOMContentLoaded', (event) => {
    // Функция для установки прогресса загрузки
    function setProgress(selector, percent) {
        const progressElement = document.querySelector(selector + ' .progress-ring__progress');
        if (progressElement) {
            const circumference = 2 * Math.PI * progressElement.r.baseVal.value;
            const offset = circumference - (percent / 100) * circumference;
            progressElement.style.strokeDashoffset = offset;
        }
    }

    // Обработчик клика для элементов с классом .fileDownloader__trigger
    document.querySelectorAll('.fileDownloader__trigger').forEach(trigger => {
        trigger.addEventListener('click', function(e) {
            e.preventDefault(); // Предотвращаем стандартное поведение ссылки
            // Находим ближайший родительский элемент с классом .fileDownloader
            const fileDownloader = this.closest('.fileDownloader');
            if (fileDownloader) {
                // Переключаем класс .progressing для родительского элемента
                fileDownloader.classList.toggle('progressing');

                // Переключаем классы для SVG иконок и круга прогресса
                const svgProgress = fileDownloader.querySelector('svg.progress-ring, svg.progress-ring--hidden');
                const svgIcon = this.querySelector('svg');
                const useElement = svgIcon.querySelector('use');

                if (svgProgress && svgIcon && useElement) {
                    svgProgress.classList.toggle('progress-ring--hidden');
                    svgProgress.classList.toggle('progress-ring');
                    svgIcon.classList.toggle('icon--downloadCircle');
                    svgIcon.classList.toggle('icon--x');

                    // Получаем текущий атрибут href и изменяем только идентификатор после #
                    const currentHref = useElement.getAttribute('href');
                    const newIcon = svgIcon.classList.contains('icon--downloadCircle') ? 'downloadCircle' : 'x';
                    useElement.setAttribute('href', currentHref.replace(/(#).*$/, `$1${newIcon}`));

                    // Если класс .progressing добавлен, обновляем прогресс
                    if (fileDownloader.classList.contains('progressing')) {
                        setProgress('.fileDownloader.progressing', 50);
                    }
                }
            }
        });
    });
});
