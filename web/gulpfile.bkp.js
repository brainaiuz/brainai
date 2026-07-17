'use strict';

var gulp = require('gulp');
var sass = require('gulp-sass');
var sourcemaps = require('gulp-sourcemaps');
var cssclean = require('gulp-clean-css');
var rename = require('gulp-rename');
// const autoprefixer = require('gulp-autoprefixer');
var bs = require('browser-sync').create();
var bsreload = bs.reload;
var watch = require('gulp-watch');

gulp.task('build', function () {
    gulp.src('./gulp-scss/materialize.scss')
        .pipe(sourcemaps.init())
        // .pipe(autoprefixer({
        //     browsers: ['last 2 versions'],
        //     cascade: false
        // }))
        .pipe(sass({noCache: true}).on('error', sass.logError))
        // .pipe(sourcemaps.write())
        .pipe(gulp.dest('./WebContent/mainStyles/new-ui/css'))
        // .pipe(gulp.dest('./../war/mainStyles/new-ui/css'))
        .pipe(bs.reload({stream: true}));
});

gulp.task('sass', function () {
    gulp.src('./gulp-scss/materialize.scss')
        .pipe(sourcemaps.init())
        // .pipe(autoprefixer({
        //     browsers: ['last 2 versions'],
        //     cascade: false
        // }))
        .pipe(sass({noCache: true}).on('error', sass.logError))
        .pipe(sourcemaps.write())
        .pipe(gulp.dest('./WebContent/mainStyles/new-ui/css'))
        .pipe(gulp.dest('./../war/mainStyles/new-ui/css'))
        .pipe(bs.reload({stream: true}));
});

gulp.task('sass:build', function () {
    gulp.src('./gulp-scss/materialize.scss')
        // .pipe(sourcemaps.init())
        // .pipe(autoprefixer({
        //     browsers: ['last 2 versions'],
        //     cascade: false
        // }))
        .pipe(sass({noCache: true}).on('error', sass.logError))
        // .pipe(sourcemaps.write())
        .pipe(cssclean())
        .pipe(rename({
            suffix: '.min'
        }))
        .pipe(gulp.dest('./WebContent/mainStyles/new-ui/css'))
        .pipe(gulp.dest('./../war/mainStyles/new-ui/css'))
        .pipe(bs.reload({stream: true}));
});

// gulp.task('grid', function () {
//     gulp.src('./gulp-scss/sass/components/grid/grid.scss')
//         .pipe(sourcemaps.init())
//         .pipe(sass().on('error', sass.logError))
//         .pipe(sourcemaps.write())
//         .pipe(gulp.dest('./WebContent/mainStyles/new-ui/css'));
// });

gulp.task('bs-reload', function () {
    gulp.src(['../war/mainStyles/new-ui/css/**/*.css']).pipe(bs.reload({stream: true}));
});

gulp.task('sass:watch', [], function () {
    bs.init({
        proxy: "localhost:8080", ghostMode: true,
        // server: "./gulp-scss/sass/**/*.scss",
        // port: 63342
    });
    gulp.watch('./gulp-scss/sass/**/*.scss', ['sass', 'sass:build']);
});

gulp.task('default', ['sass:watch'], function () {
    // gulp.watch('../war/mainStyles/new-ui/css/**/*.css', ['bs-reload']);
});

