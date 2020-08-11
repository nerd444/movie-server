const express = require("express");
const auth = require("../middleware/auth");

const {
  getMovies,
  searchMovies,
  yearMovies,
  attendanceMovies,
  getAuthMovies,
} = require("../controllers/movies");

const router = express.Router();

router.route("/").get(getMovies);
router.route("/auth").get(auth, getAuthMovies);
router.route("/search").post(searchMovies);
router.route("/year").get(yearMovies);
router.route("/desc/getMovies").get(attendanceMovies);

module.exports = router;
