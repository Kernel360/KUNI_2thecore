package hub.application;

import com.example.common.domain.car.CarEntity;
import com.example.common.infrastructure.car.CarRepository; // CarRepository import 추가
import hub.domain.GpsLogEntity;
import hub.domain.GpsLogRepository;
import hub.domain.dto.GpsLogDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional; // Optional import 추가

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*; // Mockito.when, Mockito.never 등 import

@ExtendWith(MockitoExtension.class)
class ConsumerServiceTest {

    @Mock
    private GpsLogRepository gpsLogRepository;

    @Mock // CarRepository 모의 객체 추가
    private CarRepository carRepository;

    @InjectMocks
    private ConsumerService consumerService;

    @Test
    @DisplayName("GPS 로그 소비 및 저장 테스트")
    void gpsConsumer_SaveTest() {
        // given
        GpsLogDto.Gps gps = new GpsLogDto.Gps("37.5665", "126.9780", LocalDateTime.now());
        GpsLogDto gpsLogDto = new GpsLogDto("A1234", Collections.singletonList(gps));

        // carRepository.findByCarNumber 호출 시 Optional.empty() 반환하도록 모의
        when(carRepository.findByCarNumber(gpsLogDto.getCarNumber())).thenReturn(Optional.empty());

        // when
        consumerService.gpsConsumer(gpsLogDto);

        // then
        ArgumentCaptor<List<GpsLogEntity>> captor = ArgumentCaptor.forClass(List.class);
        verify(gpsLogRepository).saveAll(captor.capture());

        List<GpsLogEntity> savedLogs = captor.getValue();
        assertEquals(1, savedLogs.size());

        GpsLogEntity savedLog = savedLogs.get(0);
        assertEquals(gpsLogDto.getCarNumber(), savedLog.getCarNumber());
        assertEquals(gps.getLatitude(), savedLog.getLatitude());
        assertEquals(gps.getLongitude(), savedLog.getLongitude());
        assertEquals(gps.getTimestamp(), savedLog.getCreatedAt());
    }

    @Test
    @DisplayName("Car 정보 업데이트 테스트 - 차량 존재 및 최신 GPS 데이터")
    void gpsConsumer_UpdateCarInfoTest() {
        // given
        String carNumber = "B5678";
        LocalDateTime initialTimestamp = LocalDateTime.now().minusMinutes(5);
        LocalDateTime newTimestamp = LocalDateTime.now();

        GpsLogDto.Gps oldGps = new GpsLogDto.Gps("37.0000", "127.0000", initialTimestamp);
        GpsLogDto.Gps newGps = new GpsLogDto.Gps("37.1234", "127.5678", newTimestamp);
        GpsLogDto gpsLogDto = new GpsLogDto(carNumber, List.of(oldGps, newGps)); // 메시지 내 여러 GPS 로그 시뮬레이션

        CarEntity existingCar = CarEntity.builder()
            .carNumber(carNumber)
            .lastLatitude("37.0000")
            .lastLongitude("127.0000")
            .build();

        when(carRepository.findByCarNumber(carNumber)).thenReturn(Optional.of(existingCar));
        // carRepository.save 호출 시 업데이트된 CarEntity를 캡처
        ArgumentCaptor<CarEntity> carCaptor = ArgumentCaptor.forClass(CarEntity.class);
        when(carRepository.save(carCaptor.capture())).thenReturn(null); // 실제 반환 값은 중요하지 않으므로 null

        // when
        consumerService.gpsConsumer(gpsLogDto);

        // then
        // GpsLogEntity 저장이 호출되었는지 확인
        verify(gpsLogRepository).saveAll(anyList());

        // CarEntity 저장이 호출되었는지 확인
        verify(carRepository).save(any(CarEntity.class));

        // 업데이트된 CarEntity의 값 검증
        CarEntity updatedCar = carCaptor.getValue();
        assertEquals(newGps.getLatitude(), updatedCar.getLastLatitude());
        assertEquals(newGps.getLongitude(), updatedCar.getLastLongitude());
    }

    @Test
    @DisplayName("Car 정보 업데이트 테스트 - 차량 미존재")
    void gpsConsumer_CarNotFoundTest() {
        // given
        String carNumber = "C9012";
        GpsLogDto.Gps gps = new GpsLogDto.Gps("37.5665", "126.9780", LocalDateTime.now());
        GpsLogDto gpsLogDto = new GpsLogDto(carNumber, Collections.singletonList(gps));

        when(carRepository.findByCarNumber(carNumber)).thenReturn(Optional.empty());

        // when
        consumerService.gpsConsumer(gpsLogDto);

        // then
        // GpsLogEntity 저장이 호출되었는지 확인
        verify(gpsLogRepository).saveAll(anyList());
        // CarEntity 저장이 호출되지 않았는지 확인
        verify(carRepository, never()).save(any(CarEntity.class));
    }
}
