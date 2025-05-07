package Java_write;

public class Benchmark extends Node{
    // make a circular buffer of the frames coz its efficient
    private final double[] frameDeltaBuffer;
    private int headIndex;
    private int frameBuffer;

    private double runningDeltaTotal;
    private int frameCount;
    
    public Benchmark(int frameBuffer) {
        this.frameBuffer = frameBuffer;
        this.headIndex = 0;

        this.frameDeltaBuffer = new double[frameBuffer];

        this.runningDeltaTotal = 0.0;
    }

    @Override
    public void tick(double delta, SceneTree sceneTree) {
        StringBuilder benchmarkString = new StringBuilder();
        double start = System.nanoTime();
        bench(delta, benchmarkString);
        benchmarkString.append(String.format("Benchmarking Took %.6fms", (System.nanoTime() - start) / 1e6));

        System.out.print(benchmarkString);
    }

    public void bench(double delta, StringBuilder benchmarkString) {
        addFrameDelta(delta);

        double averageDelta = runningDeltaTotal/frameCount; // frame count should reach and stay at frameBuffer
        benchmarkString.append(String.format("JAVA:%nAverageFPS = %.2f%nAverageDelta = %.6fms%n", 1/averageDelta, averageDelta*1e3));
    }

    public void addFrameDelta(double delta) {
        if (frameCount < frameBuffer) {
            frameCount++;
        }

        runningDeltaTotal -= frameDeltaBuffer[headIndex]; // if we havent gone through count frames yet, then it should just minus 0.0 which does nothing.
        runningDeltaTotal += delta;

        frameDeltaBuffer[headIndex] = delta;
        this.headIndex = (this.headIndex+1) % this.frameBuffer; // increase the write index by 1, but wrap around to 0 if we reach the end of the list
    }
}
