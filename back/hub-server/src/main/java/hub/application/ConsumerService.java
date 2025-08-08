package hub.application;


import com.example.common.domain.car.CarEntity;
import com.example.common.infrastructure.car.CarRepository;
import hub.domain.GpsLogEntity;
import hub.domain.GpsLogRepository;
import hub.domain.dto.GpsLogDto;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConsumerService {

    private final GpsLogRepository gpsLogRepository;
    private final CarRepository carRepository;

    @RabbitListener(queues = "gps.data.queue", errorHandler = "gpsConsumerErrorHandler")
    public void gpsConsumer(GpsLogDto gpsLogDto) {

        log.info("Message received from RabbitMQ: {}", gpsLogDto);

        // 주기(60초,120초,180초) 단위 DB 저장
        List<GpsLogEntity> gpsLogEntities = gpsLogDto.getLogList().stream()
            .map(gps -> new GpsLogEntity(
                gpsLogDto.getCarNumber(),
                gps.getLatitude(),
                gps.getLongitude(),
                gps.getTimestamp()))
            .collect(Collectors.toList());

        gpsLogRepository.saveAll(gpsLogEntities);

        // Car table의 칼럼 last_latitude, last_longitude 갱신
        String carNumber = gpsLogDto.getCarNumber();
        Optional<CarEntity> optionalCar = carRepository.findByCarNumber(carNumber);

        if (optionalCar.isPresent()) {
            CarEntity carEntity = optionalCar.get();

            // 수신된 GPS 데이터 중 가장 최신 timestamp를 가진 데이터 조회
            Optional<GpsLogDto.Gps> latestReceivedGps = gpsLogDto.getLogList().stream()
                .max(Comparator.comparing(GpsLogDto.Gps::getTimestamp));

            //cartable 수정
            if (latestReceivedGps.isPresent()) {
                GpsLogDto.Gps receivedGps = latestReceivedGps.get();

                carEntity.setLastLatitude(receivedGps.getLatitude());
                carEntity.setLastLongitude(receivedGps.getLongitude());
                carRepository.save(carEntity);
                log.info("Car {} last_latitude, last_longitude updated to {}, {}", carNumber, receivedGps.getLatitude(), receivedGps.getLongitude());
            }
        } else {
            log.warn("Car with carNumber {} not found. Cannot update last_latitude and last_longitude.", carNumber);
        }
    }
}

