package com.delfino.controller;

import com.delfino.dao.LogsDao;
import com.delfino.model.*;
import com.delfino.util.UiLogFormatter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(path = {"", "/"})
@Controller
public class HomeController {

    private final LogsDao logsDao = new LogsDao();
    private final UiLogFormatter logFormatter = new UiLogFormatter();
    private final ObjectMapper mapper = logFormatter.getMapper();

    @Value("${timezone}")
    private String timezone;

    @PostConstruct
    public void initialize() {

        SimpleDateFormat displayFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        displayFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        mapper.setDateFormat(displayFormat);
    }

    @GetMapping({"/", "/index"})
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("timezone", timezone);
        return mav;
    }

    @GetMapping("/latest_stats")
    public ModelAndView latestStats() throws ParseException {

        BabyStats stats = logsDao.getLatestStats();

        ModelAndView mav = new ModelAndView("summary");
        mav.addObject("age", stats.getAgeInMonths() + " months / " + stats.getAgeInWeeks() + " weeks");
        mav.addObject("correctedAge", stats.getCorrectedAgeInMonths() + " months / " + stats.getCorrectedAgeInWeeks() + " weeks");
        mav.addObject("lastFeedingTime", stats.getLastFeedingTime());
        mav.addObject("lastFeedingTimeAgo", stats.getLastFeedingTimeAgo());
        mav.addObject("lastFeedingVolume", stats.getLastFeedingVolume());
        mav.addObject("weight", stats.getHealthStats().getWeight() + " kg");
        mav.addObject("height", stats.getHealthStats().getHeight() + " cm");
        mav.addObject("headCircumference", stats.getHealthStats().getHeadCircumference() + " cm");
        mav.addObject("jaundiceLevel", stats.getHealthStats().getJaundiceLevel());
        return mav;
    }

    @GetMapping("/latest_stats_json")
    @ResponseBody
    public String latestStatsJson() throws JsonProcessingException {

        return mapper.writeValueAsString(logsDao.getLatestStats());
    }


    @GetMapping("/logs")
    @ResponseBody
    public String logs() throws JsonProcessingException {


        return mapper.writeValueAsString(logFormatter.formatLogs(logsDao.getAll()));
    }

    @GetMapping("/log")
    @ResponseBody
    public String getLog(long id) throws JsonProcessingException {

        return mapper.writeValueAsString(logsDao.getById(id));
    }

    @PostMapping("/duplicate")
    @ResponseBody
    public boolean duplicateLog(long id) throws IOException {

        return logsDao.duplicateLog(id);
    }

    @PostMapping("/add_feeding")
    @ResponseBody
    public boolean add(LogFeedingEntry entry) {

        return logsDao.addLog(entry);
    }

    @PostMapping("/add_medication")
    @ResponseBody
    public boolean add(LogMedicationEntry entry) {

        return logsDao.addLog(entry);
    }

    @PostMapping("/add_vaccine")
    @ResponseBody
    public boolean add(LogVaccineEntry entry) {

        return logsDao.addLog(entry);
    }

    @PostMapping("/add_clinic-visit")
    @ResponseBody
    public boolean add(LogClinicVisitEntry entry) {

        return logsDao.addLog(entry);
    }

    @PostMapping("/add_health-stats")
    @ResponseBody
    public boolean add(LogHealthStatsEntry entry) {

        return logsDao.addLog(entry);
    }

    @PostMapping("/add_others")
    @ResponseBody
    public boolean add(LogEntry entry) {

        return logsDao.addLog(entry);
    }

    @PostMapping("/update_feeding")
    @ResponseBody
    public boolean update(LogFeedingEntry entry) {

        return logsDao.updateLog(entry.getId(), entry);
    }

    @PostMapping("/update_medication")
    @ResponseBody
    public boolean update(LogMedicationEntry entry) {

        return logsDao.updateLog(entry.getId(), entry);
    }

    @PostMapping("/update_vaccine")
    @ResponseBody
    public boolean update(LogVaccineEntry entry) {

        return logsDao.updateLog(entry.getId(), entry);
    }

    @PostMapping("/update_clinic-visit")
    @ResponseBody
    public boolean update(LogClinicVisitEntry entry) {

        return logsDao.updateLog(entry.getId(), entry);
    }

    @PostMapping("/update_health-stats")
    @ResponseBody
    public boolean update(LogHealthStatsEntry entry) {

        return logsDao.updateLog(entry.getId(), entry);
    }

    @PostMapping("/update_others")
    @ResponseBody
    public boolean update(LogEntry entry) {

        return logsDao.updateLog(entry.getId(), entry);
    }

    @PostMapping("/delete")
    @ResponseBody
    public boolean deleteLog(Long id) {

        return logsDao.deleteLog(id);
    }

    @PostMapping("/search")
    @ResponseBody
    public String search(String query) throws JsonProcessingException {
        return mapper.writeValueAsString(logFormatter.formatLogs(logsDao.searchByKeyword(query)));
    }
}
